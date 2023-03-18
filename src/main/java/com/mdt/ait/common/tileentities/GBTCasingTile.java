package com.mdt.ait.common.tileentities;

import com.mdt.ait.core.init.AITTiles;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class GBTCasingTile extends TileEntity implements ITickableTileEntity {

    public GBTCasingTile() {
        super(AITTiles.GBTCASING_TILE_ENTITY_TYPE.get());
    }

    public boolean make_thing_spin = false;
    public float spin_value = 0;

    @Override
    public void tick() {
        BlockPos blockPos = new BlockPos(worldPosition.below(1));
        make_thing_spin =
                level.canSeeSky(worldPosition) && level.getBlockState(blockPos).getBlock() instanceof SoulSandBlock;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition).inflate(10, 10, 10);
    }
}
