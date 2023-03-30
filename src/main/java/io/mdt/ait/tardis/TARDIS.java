package io.mdt.ait.tardis;

import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializer;
import io.mdt.ait.nbt.wrapped.AbsoluteBlockPos;
import io.mdt.ait.nbt.wrapped.NBTSerializers;
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.exterior.TARDISExterior;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.interior.TARDISInterior;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import io.mdt.ait.tardis.state.TARDISStateManager;
import io.mdt.ait.util.TARDISUtil;
import java.util.UUID;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TARDIS {

    private final UUID uuid;

    private final TARDISExterior exterior;
    private final TARDISInterior interior;
    private final TARDISDoor door;

    private final TARDISStateManager stateManager = new TARDISStateManager();
    private final TARDISTravel travelManager = new TARDISTravel();

    private AbsoluteBlockPos position;

    public TARDIS(
            UUID uuid,
            BlockPos position,
            RegistryKey<World> dimension,
            TARDISExteriorSchema<?> exterior,
            TARDISInteriorSchema interior) {
        this(
                new CompoundNBT(),
                uuid,
                new AbsoluteBlockPos(dimension, position),
                new TARDISExterior(exterior),
                new TARDISInterior(interior),
                new TARDISDoor(TARDISUtil.getInteriorPos(interior).offset(interior.getDoorPosition())),
                true);

        this.interior.getSchema().place(TARDISUtil.getTARDISWorld());

        // Duplicates linking process, because the interior needs to be placed before linking the door
        // and other stuff
        this.door.link(this);
        this.exterior.link(this);
        this.interior.link(this);
    }

    private TARDIS(
            CompoundNBT raw,
            UUID uuid,
            AbsoluteBlockPos position,
            TARDISExterior exterior,
            TARDISInterior interior,
            TARDISDoor door,
            boolean firstTime) {
        this.uuid = uuid;
        this.position = position;

        this.exterior = exterior;
        this.interior = interior;

        this.door = door;

        if (!firstTime) {
            this.door.link(this);
            this.exterior.link(this);
            this.interior.link(this);
        }

        new TARDISStateManager.Serializer().unserialize(raw, this.stateManager);
        this.travelManager.link(this); // deserialize(?)
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public AbsoluteBlockPos getPosition() {
        return this.position;
    }

    public void setPosition(AbsoluteBlockPos position) {
        this.position = position;
    }

    public TARDISExterior getExterior() {
        return this.exterior;
    }

    public TARDISInterior getInterior() {
        return this.interior;
    }

    public TARDISDoor getDoor() {
        return this.door;
    }

    public TARDISStateManager getStateManager() {
        return this.stateManager;
    }

    public TARDISTravel getTravelManager() {
        return this.travelManager;
    }

    public boolean ownsKey(ItemStack key) {
        CompoundNBT nbt = key.getOrCreateTag();
        if (nbt.hasUUID("uuid")) {
            return nbt.getUUID("uuid").equals(this.uuid);
        }

        return false;
    }

    public static class Serializer implements NBTSerializer<TARDIS>, NBTDeserializer<TARDIS> {

        private static final NBTSerializers.AbsolutePosition ABSOLUTE_POSITION_SERIALIZER =
                new NBTSerializers.AbsolutePosition();
        private static final TARDISExterior.Serializer EXTERIOR_SERIALIZER = new TARDISExterior.Serializer();
        private static final TARDISInterior.Serializer INTERIOR_SERIALIZER = new TARDISInterior.Serializer();
        private static final TARDISDoor.Serializer DOOR_SERIALIZER = new TARDISDoor.Serializer();

        private static final TARDISStateManager.Serializer STATE_SERIALIZER = new TARDISStateManager.Serializer();

        @Override
        public void serialize(CompoundNBT nbt, TARDIS tardis) {
            nbt.putUUID("uuid", tardis.getUUID());
            ABSOLUTE_POSITION_SERIALIZER.serialize(nbt, tardis.position);

            nbt.put("exterior", EXTERIOR_SERIALIZER.serialize(tardis.exterior));
            nbt.put("interior", INTERIOR_SERIALIZER.serialize(tardis.interior));
            nbt.put("door", DOOR_SERIALIZER.serialize(tardis.door));
            nbt.put("state", STATE_SERIALIZER.serialize(tardis.stateManager));
        }

        @Override
        public TARDIS deserialize(CompoundNBT nbt) {
            // State de-serialization happens in the constructor.
            return new TARDIS(
                    nbt,
                    nbt.getUUID("uuid"),
                    ABSOLUTE_POSITION_SERIALIZER.deserialize(nbt),
                    EXTERIOR_SERIALIZER.deserialize(nbt.getCompound("exterior")),
                    INTERIOR_SERIALIZER.deserialize(nbt.getCompound("interior")),
                    DOOR_SERIALIZER.deserialize(nbt.getCompound("door")),
                    false);
        }
    }
}
