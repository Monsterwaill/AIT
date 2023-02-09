package io.mdt.ait.tardis;

import io.mdt.ait.NBTSerializeable;
import io.mdt.ait.NBTUnserializeable;
import io.mdt.ait.common.tiles.TARDISInteriorDoorTile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class TARDISDoor {

    private TARDISInteriorDoorTile tile;
    private final BlockPos position;

    private final DoorState state = new DoorState();
    private final DoublePortal portal = new DoublePortal();

    public TARDISDoor(BlockPos position) {
        this.position = position;
    }

    /**
     * Explicit link method.
     *
     * @param tardis {@link TARDIS} that the component will link to.
     */
    public void link(TARDIS tardis) {
        this.tile = (TARDISInteriorDoorTile) TARDISUtil.getTARDISWorld().getBlockEntity(this.position);
        if (this.tile != null) {
            this.tile.getLink().link(tardis);
        }
    }

    public BlockPos getPosition() {
        return this.position;
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
        public CompoundNBT serialize(TARDISDoor tardisDoor, CompoundNBT nbt) {
            nbt.putInt("state", tardisDoor.state.getRaw());
            nbt.putLong("position", tardisDoor.position.asLong());

            return nbt;
        }

        @Override
        public TARDISDoor unserialize(CompoundNBT nbt) {
            TARDISDoor door = new TARDISDoor(BlockPos.of(nbt.getLong("position")));
            door.state.fromRaw(nbt.getInt("state"));

            return door;
        }
    }
}
