package io.mdt.ait.tardis;

import io.mdt.ait.nbt.NBTSerializeable;
import io.mdt.ait.nbt.NBTUnserializeable;
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TARDIS {

    private final UUID uuid;

    private TARDISExteriorSchema exterior;
    private TARDISInteriorSchema interior;
    private final TARDISDoor door;

    private BlockPos position;
    private RegistryKey<World> dimension;

    public TARDIS(UUID uuid, BlockPos position, RegistryKey<World> dimension, TARDISExteriorSchema exterior, TARDISInteriorSchema interior) {
        this(uuid, position, dimension, exterior, interior, new TARDISDoor(
                TARDISUtil.getInteriorPos(interior).offset( // FIXME: This is wrong
                        interior.getDoorPosition().getX(),
                        interior.getDoorPosition().getY(),
                        interior.getDoorPosition().getZ()
                )
        ), false);

        System.out.println(TARDISUtil.getInteriorPos(this.interior));
        System.out.println(exterior);
        this.interior.place(TARDISUtil.getTARDISWorld(), TARDISUtil.getInteriorPos(this.interior));
        this.door.link(this);
    }

    private TARDIS(UUID uuid, BlockPos position, RegistryKey<World> dimension, TARDISExteriorSchema exterior, TARDISInteriorSchema interior, TARDISDoor door, boolean init) {
        this.uuid = uuid;
        this.position = position;
        this.dimension = dimension;

        this.exterior = exterior;
        this.interior = interior;

        this.door = door;

        if (init) {
            this.door.link(this);
        }
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public BlockPos getPosition() {
        return this.position;
    }
    public RegistryKey<World> getDimension() {
        return this.dimension;
    }

    public TARDISExteriorSchema getExterior() {
        return this.exterior;
    }

    public void setExterior(TARDISExteriorSchema exterior) {
        this.exterior = exterior;
    }

    public TARDISInteriorSchema getInterior() {
        return this.interior;
    }

    public void setInterior(TARDISInteriorSchema interior) {
        this.interior = interior;
    }

    public TARDISDoor getDoor() {
        return this.door;
    }


    public boolean ownsKey(ItemStack key) {
        CompoundNBT nbt = key.getOrCreateTag();
        if (nbt.hasUUID("uuid")) {
            return nbt.getUUID("uuid").equals(this.uuid);
        }

        return false;
    }

    public static class Serializer implements NBTSerializeable<TARDIS>, NBTUnserializeable<TARDIS> {

        private static final TARDISExteriorSchema.Serializer exteriorSerializer =  new TARDISExteriorSchema.Serializer();
        private static final TARDISInteriorSchema.Serializer interiorSerializer =  new TARDISInteriorSchema.Serializer();
        private static final TARDISDoor.Serializer doorSerializer = new TARDISDoor.Serializer();

        @Override
        public void serialize(TARDIS tardis, CompoundNBT nbt) {
            nbt.putUUID("uuid", tardis.getUUID());
            nbt.putLong("position", tardis.position.asLong());

            CompoundNBT dimensionNBT = new CompoundNBT();
            dimensionNBT.putString("name", tardis.dimension.getRegistryName().toString());
            dimensionNBT.putString("location", tardis.dimension.location().toString());
            nbt.put("dimension", dimensionNBT);

            nbt.put("exterior", exteriorSerializer.serialize(tardis.exterior));
            nbt.put("interior", interiorSerializer.serialize(tardis.interior));
            nbt.put("door", doorSerializer.serialize(tardis.door));
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
                    doorSerializer.unserialize(nbt.getCompound("door")), true
            );
        }
    }
}
