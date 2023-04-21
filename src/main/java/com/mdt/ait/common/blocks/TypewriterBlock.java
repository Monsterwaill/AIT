package com.mdt.ait.common.blocks;

import com.mdt.ait.AIT;
import com.mdt.ait.client.screen.MonitorScreen;
import com.mdt.ait.common.tileentities.TypewriterTile;
import com.mdt.ait.common.tileentities.TSVTile;
import com.mdt.ait.common.tileentities.TardisTileEntity;
import com.mdt.ait.common.tileentities.TypewriterTile;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class TypewriterBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static VoxelShape YES_SHAPE = Block.box(0, 0, 0, 16, 9.5, 16);

    //public UUID tardisID;

    public TypewriterBlock() {
        super(Properties.of(Material.STONE).strength(15.0f).noOcclusion().instabreak());
    }

    @Override
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return YES_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return YES_SHAPE;
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
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /*@Override
    public void onPlace(BlockState blockState1, World world, BlockPos blockPos, BlockState blockState2, boolean bool) {
        super.onPlace(blockState1, world, blockPos, blockState2, bool);
        if (!world.isClientSide && world.dimension() == AITDimensions.TARDIS_DIMENSION) {
            ServerWorld serverWorld = ((ServerWorld) world);
            TypewriterTile typewriterTile = (TypewriterTile) serverWorld.getBlockEntity(blockPos);
            this.tardisID = AIT.tardisManager.getTardisIDFromPosition(blockPos);
            assert typewriterTile != null;
            typewriterTile.tardisID = tardisID;
            serverWorld.setBlockEntity(blockPos, typewriterTile);
        }
    }*/

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TypewriterTile();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
                                Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide()) {
            Block block = worldIn.getBlockState(pos).getBlock();
            if (block instanceof TypewriterBlock) {
                //NetworkHandler.CHANNEL.sendToServer();
                //Minecraft.getInstance().setScreen(new MonitorScreen(new TranslationTextComponent(
                //        "TARDIS Monitor")));
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }
}
