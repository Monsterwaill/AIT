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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TARDISBlock extends FallingBlock implements ITARDISBlock, ITARDISLinked { // ITardisBlock has some of the same functionality as interface ICantBreak

    public UUID tardisID;

    private static final Map<Direction, VoxelShape> directionShapeMap = new HashMap<Direction, VoxelShape>() {{
        put(Direction.NORTH, VoxelShapes.create(new AxisAlignedBB(0, 0, 0.00625, 1, 2, 1)));
        put(Direction.EAST, VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 0.99375, 2, 1)));
        put(Direction.SOUTH, VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 2, 0.99375)));
        put(Direction.WEST, VoxelShapes.create(new AxisAlignedBB(0.00625, 0, 0, 1, 2, 1)));
    }};

    public TARDISBlock() {
        super(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noOcclusion());
    }

    @Override
    public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving) {
        TARDISTileEntity tardisTileEntity = (TARDISTileEntity) level.getBlockEntity(pos);
        if(tardisTileEntity != null && level instanceof ServerWorld) {
            tardisTileEntity.link(TARDISManager.create(pos, level.dimension()));
        }

        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public ActionResultType use(BlockState pState, World pWorldIn, BlockPos pPos, PlayerEntity pPlayer, Hand pHandIn, BlockRayTraceResult pHit) {
        TileEntity tileEntity = pWorldIn.getBlockEntity(pPos);
        if (tileEntity instanceof TARDISTileEntity) {
            //((TARDISTileEntity) tileEntity).use(pWorldIn, pPlayer, pPos, pHandIn); FIXME: this
        }
        return super.use(pState, pWorldIn, pPos, pPlayer, pHandIn, pHit);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return TARDISBlock.directionShapeMap.get(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TARDISTileEntity();
    }

    private final TARDISLink link = new TARDISLink();

    @Override
    public TARDISLink getLink() {
        return null;
    }
}
