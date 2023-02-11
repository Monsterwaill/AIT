package io.mdt.ait.tardis;

import io.mdt.ait.NBTSerializeable;
import io.mdt.ait.NBTUnserializeable;
import io.mdt.ait.common.tiles.TARDISInteriorDoorTile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;

public class TARDISDoor {

    private TARDISInteriorDoorTile tile;
    private final BlockPos doorPosition;

    private final DoorState state = new DoorState();
    private final DoublePortal portal = new DoublePortal();

    public TARDISDoor(BlockPos doorPosition) {
        this.doorPosition = doorPosition;
    }

    public void link(TARDIS tardis) {
        this.tile = (TARDISInteriorDoorTile) TARDISUtil.getTARDISWorld().getBlockEntity(doorPosition);
        if (this.tile != null) {
            this.tile.getLink().link(tardis);
        }
    }

    public BlockPos getDoorPosition() {
        return this.doorPosition;
    }

    public TARDISInteriorDoorTile getTile() {
        return this.tile;
    }

    public void setTile(TARDISInteriorDoorTile tile) {
        this.tile = tile;
    }

    public DoorState getState() {
        return this.state;
    }

    public DoublePortal getPortal() {
        return this.portal;
    }

    public static class Serializer implements NBTSerializeable<TARDISDoor>, NBTUnserializeable<TARDISDoor> {

        @Override
        public CompoundNBT serialize(TARDISDoor door, CompoundNBT nbt) {
            nbt.putInt("state", door.state.getRaw());
            nbt.putLong("door_position", door.getTile().getBlockPos().asLong());

            return nbt;
        }

        @Override
        public TARDISDoor unserialize(CompoundNBT nbt) {
            TARDISDoor door = new TARDISDoor(BlockPos.of(nbt.getLong("door_position")));
            door.state.fromRaw(nbt.getInt("state"));

            return door;
        }
    }
}
