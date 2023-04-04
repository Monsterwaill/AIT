package com.mdt.ait.common.blocks;

import io.mdt.ait.common.tiles.TARDISInteriorDoorTile;
import java.util.UUID;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BasicInteriorDoorBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public UUID tardisID;

    /*
     * public static final VoxelShape NORTH_AABB = VoxelShapes.box(0, 0, 0, 16, 32, 16); public
     * static final VoxelShape EAST_AABB = VoxelShapes.box(0, 0, 0, 16, 32, 16); public static final
     * VoxelShape SOUTH_AABB = VoxelShapes.box(0, 0, 0, 16, 32, 16); public static final VoxelShape
     * WEST_AABB = VoxelShapes.box(0, 0, 0, 16, 32, 16);
     *
     * public static final VoxelShape SOUTH_AABB = VoxelShapes.create(new AxisAlignedBB(0, 0,
     * -0.0625, 1, 2, -0.006249999999999978)); public static final VoxelShape WEST_AABB =
     * VoxelShapes.create(new AxisAlignedBB(1.00625, 0, 0, 1.0625, 2, 1)); public static final
     * VoxelShape NORTH_AABB = VoxelShapes.create(new AxisAlignedBB(0, 0, 1.00625, 1, 2, 1.0625));
     * public static final VoxelShape EAST_AABB = VoxelShapes.create(new AxisAlignedBB(-0.0625, 0,
     * 0, -0.006249999999999978, 2, 1));
     */

    public static final VoxelShape SOUTH_AABB = VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 2, 1));
    public static final VoxelShape WEST_AABB = VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 2, 1));
    public static final VoxelShape NORTH_AABB = VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 2, 1));
    public static final VoxelShape EAST_AABB = VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 2, 1));

    public BasicInteriorDoorBlock() {
        super(Properties.of(Material.STONE)
                .strength(15.0f)
                .noOcclusion()
                .instabreak()
                .noCollission()
                .lightLevel(BlockState -> 15));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        switch (pState.getValue(FACING)) {
            case NORTH:
                return NORTH_AABB;
            case EAST:
                return EAST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
        }

        return null; // How did you get there?
    }

    @Override
    public ActionResultType use(
            BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TARDISInteriorDoorTile && hand == Hand.MAIN_HAND) {
            return ((TARDISInteriorDoorTile) tile).use(player, pos);
        }

        return ActionResultType.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean bool) {
        /*
         * if (world.dimension() == AITDimensions.TARDIS_DIMENSION && !world.isClientSide) {
         * world.setBlockEntity(pos, world.getBlockEntity(pos)); }
         */
        // Probably redundant
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TARDISInteriorDoorTile();
    }
}
