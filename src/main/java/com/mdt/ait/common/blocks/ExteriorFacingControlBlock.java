package com.mdt.ait.common.blocks;

import com.mdt.ait.common.tileentities.ExteriorFacingControlTile;
import io.mdt.ait.tardis.TARDISManager;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ExteriorFacingControlBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = VoxelShapes.or(Block.box(0, 0, 0, 16, 2, 16), Block.box(1, 2, 1, 15, 4, 15))
            .optimize();

    public ExteriorFacingControlBlock() {
        super(Properties.of(Material.STONE).strength(15.0f).noOcclusion());
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public ActionResultType use(
            BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof ExteriorFacingControlTile && hand == Hand.MAIN_HAND) {
            ((ExteriorFacingControlTile) tileEntity).used(player, pos, hand);
        }

        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            ExteriorFacingControlTile tile = (ExteriorFacingControlTile) level.getBlockEntity(pos);

            if (tile != null) {
                tile.link(TARDISManager.getInstance().findTARDIS(pos));
            }
        }
    }

    @Nullable @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ExteriorFacingControlTile();
    }
}
