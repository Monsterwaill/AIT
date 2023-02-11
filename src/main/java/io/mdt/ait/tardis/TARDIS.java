package io.mdt.ait.tardis;

import io.mdt.ait.NBTSerializeable;
import io.mdt.ait.NBTUnserializeable;
import io.mdt.ait.tardis.exterior.TARDISExterior;
import io.mdt.ait.tardis.interior.TARDISInterior;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TARDIS {

    private final UUID uuid;

    private TARDISExterior exterior;
    private TARDISInterior interior;
    private final TARDISDoor door;

    private BlockPos position;
    private RegistryKey<World> dimension;

    public TARDIS(UUID uuid, BlockPos position, RegistryKey<World> dimension, TARDISExterior exterior, TARDISInterior interior) {
        this(uuid, position, dimension, exterior, interior, new TARDISDoor(
                TARDISUtil.getInteriorPos(interior).offset(
                        -interior.getCenter().getX() + interior.getDoorPosition().getX(),
                        -interior.getCenter().getY() + interior.getDoorPosition().getY(),
                        -interior.getCenter().getZ() + interior.getDoorPosition().getZ()
                )
        ));

        this.interior.place(TARDISUtil.getTARDISWorld(), TARDISUtil.getInteriorPos(this.interior));
    }

    private TARDIS(UUID uuid, BlockPos position, RegistryKey<World> dimension, TARDISExterior exterior, TARDISInterior interior, TARDISDoor door) {
        this.uuid = uuid;
        this.position = position;
        this.dimension = dimension;

        this.exterior = exterior;
        this.interior = interior;

        this.door = door;
        this.door.link(this);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public TARDISExterior getExterior() {
        return this.exterior;
    }

    public void setExterior(TARDISExterior exterior) {
        this.exterior = exterior;
    }

    public TARDISInterior getInterior() {
        return this.interior;
    }

    public void setInterior(TARDISInterior interior) {
        this.interior = interior;
    }

    public TARDISDoor getDoor() {
        return this.door;
    }

    public RegistryKey<World> getDimension() {
        return this.dimension;
    }

    public boolean ownsKey(ItemStack key) {
        CompoundNBT nbt = key.getOrCreateTag();
        if (nbt.hasUUID("uuid")) {
            return nbt.getUUID("uuid").equals(this.uuid);
        }

        return false;
    }

    public static class Serializer implements NBTSerializeable<TARDIS>, NBTUnserializeable<TARDIS> {

        private static final TARDISExterior.Serializer exteriorSerializer =  new TARDISExterior.Serializer();
        private static final TARDISInterior.Serializer interiorSerializer =  new TARDISInterior.Serializer();
        private static final TARDISDoor.Serializer doorSerializer = new TARDISDoor.Serializer();

        @Override
        public CompoundNBT serialize(TARDIS tardis, CompoundNBT nbt) {
            nbt.putUUID("uuid", tardis.getUUID());
            nbt.putLong("position", tardis.position.asLong());

            CompoundNBT dimensionNBT = new CompoundNBT();
            dimensionNBT.putString("name", tardis.dimension.getRegistryName().toString());
            dimensionNBT.putString("location", tardis.dimension.location().toString());
            nbt.put("dimension", dimensionNBT);

            nbt.put("exterior", exteriorSerializer.serialize(tardis.exterior));
            nbt.put("interior", interiorSerializer.serialize(tardis.interior));
            nbt.put("door", doorSerializer.serialize(tardis.door));
            return nbt;
        }

        @Override
        public TARDIS unserialize(CompoundNBT nbt) {
            CompoundNBT dimensionNBT = nbt.getCompound("dimension");
            RegistryKey<World> dimension = RegistryKey.create(
                    RegistryKey.createRegistryKey(
                            new ResourceLocation(dimensionNBT.getString("name"))
                    ),
                    new ResourceLocation(dimensionNBT.getString("location"))
            );

            return new TARDIS(
                    nbt.getUUID("uuid"),
                    BlockPos.of(nbt.getLong("position")), dimension,
                    exteriorSerializer.unserialize(nbt.getCompound("exterior")),
                    interiorSerializer.unserialize(nbt.getCompound("interior")),
                    doorSerializer.unserialize(nbt.getCompound("door"))
            );
        }
    }
}
