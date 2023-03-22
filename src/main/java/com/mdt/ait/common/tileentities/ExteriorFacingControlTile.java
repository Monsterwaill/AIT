package com.mdt.ait.common.tileentities;

import com.mdt.ait.common.tileentities.state.ExteriorFacingControlState;
import com.mdt.ait.core.init.AITTiles;
import io.mdt.ait.tardis.link.impl.stateful.TARDISComponent;
import io.mdt.ait.tardis.state.TARDISComponentState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

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

    public ActionResultType used(PlayerEntity player, BlockPos pos, Hand hand) {
        if (this.isLinked()) {
            player.displayClientMessage(
                    new TranslationTextComponent(String.valueOf(this.getDirection()))
                            .setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)),
                    true);
            this.getExterior().getTile().setFacing(this.getNextDirection());
            this.getExterior().getTile().sync();
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    public Direction getDirection() {
        if (this.isLinked()) {
            System.out.println("TARDIS ID: " + this.getExterior().getTARDIS().getUUID());
            System.out.println("-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-");
            return this.getExterior().getTile().getFacing();
        } else {
            return null;
        }
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
