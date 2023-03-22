package com.mdt.ait.common.tileentities;

import com.mdt.ait.common.tileentities.state.ExteriorFacingControlState;
import com.mdt.ait.core.init.AITTiles;
import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.tardis.TARDISManager;
import io.mdt.ait.tardis.link.impl.stateful.TARDISComponent;
import io.mdt.ait.tardis.state.TARDISComponentState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ExteriorFacingControlTile extends TARDISComponent<ExteriorFacingControlState> {

    public ExteriorFacingControlTile() {
        super(AITTiles.EXTERIOR_FACING_CONTROL_TILE_ENTITY_TYPE.get());
    }

    @Override
    public void onLoad() {
        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            ExteriorFacingControlTile tile =
                    (ExteriorFacingControlTile) this.getLevel().getBlockEntity(this.getBlockPos());

            if (tile != null) {
                tile.link(TARDISManager.getInstance().findTARDIS(this.getBlockPos()));
            }
        }
    }

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

    public ActionResultType used(PlayerEntity player) {
        if (this.isLinked()) {
            TARDISTileEntity tile = this.getExterior().getTile();

            player.displayClientMessage(
                    new TranslationTextComponent(this.getDirection().toString())
                            .setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)),
                    true);

            tile.setFacing(this.getNextDirection());
            tile.sync();
        }

        return ActionResultType.SUCCESS;
    }

    public Direction getDirection() {
        return this.isLinked() ? this.getExterior().getTile().getFacing() : null;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.getBlockPos()).inflate(10, 10, 10);
    }

    @Override
    public TARDISComponentState<ExteriorFacingControlState> createState() {
        return new ExteriorFacingControlState();
    }
}
