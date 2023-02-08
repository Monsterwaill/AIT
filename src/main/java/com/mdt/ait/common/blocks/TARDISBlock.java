package com.mdt.ait.common.blocks;

import com.mdt.ait.core.init.interfaces.ITARDISBlock;
import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.tardis.ITARDISLinked;
import io.mdt.ait.tardis.TARDISLink;
import io.mdt.ait.tardis.TARDISManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TARDISBlock extends FallingBlock implements ITARDISBlock, ITARDISLinked { // ITARDISBlock has some of the same functionality as interface ICantBreak

    private final TARDISLink link = new TARDISLink();

    public TARDISBlock() {
        super(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noOcclusion());
    }

    @Override
    public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving) {
        TARDISTileEntity tile = (TARDISTileEntity) level.getBlockEntity(pos);
        if(tile != null) {
            tile.link(TARDISManager.create(pos, level.dimension()));
        }

        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH: return VoxelShapes.create(new AxisAlignedBB(0, 0, 0.00625, 1, 2, 1));
            case EAST:  return VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 0.99375, 2, 1));
            case SOUTH: return VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 2, 0.99375));
            case WEST:  return VoxelShapes.create(new AxisAlignedBB(0.00625, 0, 0, 1, 2, 1));
        }

        return null; // How the hell did you get there?
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TARDISTileEntity();
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
