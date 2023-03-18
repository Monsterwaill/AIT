package com.mdt.ait.common.tileentities;

import com.mdt.ait.common.tileentities.state.ExteriorFacingControlState;
import com.mdt.ait.core.init.AITTiles;
import io.mdt.ait.tardis.link.impl.stateful.TARDISComponent;
import io.mdt.ait.tardis.state.TARDISComponentState;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public class ExteriorFacingControlTile extends TARDISComponent<ExteriorFacingControlState> {

    public ExteriorFacingControlTile() {
        super(AITTiles.EXTERIOR_FACING_CONTROL_TILE_ENTITY_TYPE.get());
    }

    // TODO: make a cycled list or smth
    private Direction getNextDirection() {
        switch (this.getDirection()) {
            case NORTH:
                return Direction.EAST;
            case EAST:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.WEST;
            case WEST:
                return Direction.NORTH;
        }

        return Direction.NORTH;
    }

    public ActionResultType used() {
        this.getExterior().getTile().setFacing(this.getNextDirection());
        return ActionResultType.SUCCESS;
    }

    public Direction getDirection() {
        return this.getExterior().getTile().getFacing();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition).inflate(10, 10, 10);
    }

    @Override
    public TARDISComponentState<ExteriorFacingControlState> createState() {
        return new ExteriorFacingControlState();
    }
}
