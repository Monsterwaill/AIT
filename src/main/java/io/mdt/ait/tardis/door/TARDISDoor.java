package io.mdt.ait.tardis.door;

import io.mdt.ait.common.tiles.TARDISInteriorDoorTile;
import io.mdt.ait.nbt.NBTSerializeable;
import io.mdt.ait.nbt.NBTUnserializeable;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.link.impl.TARDISLinkableBasic;
import io.mdt.ait.tardis.portal.DoublePortal;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class TARDISDoor extends TARDISLinkableBasic {

    private TARDISInteriorDoorTile tile;
    private final BlockPos position;

    private final TARDISDoorState state;
    private final DoublePortal portal = new DoublePortal();

    public TARDISDoor(BlockPos position) {
        this(position, new TARDISDoorState());
    }

    private TARDISDoor(BlockPos position, TARDISDoorState state) {
        this.position = position;
        this.state = state;
    }

    public void link(TARDIS tardis) {
        super.link(tardis);

        this.tile = (TARDISInteriorDoorTile) TARDISUtil.getTARDISWorld().getBlockEntity(position);
        if (this.tile != null) {
            this.tile.link(tardis);
        }
    }

    public BlockPos getDoorPosition() {
        return this.position;
    }

    public TARDISInteriorDoorTile getTile() {
        return this.tile;
    }

    public TARDISDoorState getState() {
        return this.state;
    }

    public DoublePortal getPortal() {
        return this.portal;
    }

    public static class Serializer implements NBTSerializeable<TARDISDoor>, NBTUnserializeable<TARDISDoor> {

        private static final TARDISDoorState.Serializer STATE_SERIALIZER = new TARDISDoorState.Serializer();

        @Override
        public void serialize(CompoundNBT nbt, TARDISDoor door) {
            STATE_SERIALIZER.serialize(nbt, door.state);
            nbt.putLong("tile", door.getTile().getBlockPos().asLong());
        }

        @Override
        public TARDISDoor unserialize(CompoundNBT nbt) {
            return new TARDISDoor(BlockPos.of(nbt.getLong("tile")), STATE_SERIALIZER.unserialize(nbt));
        }
    }
}
