package com.mdt.ait.core.init.events;

import com.google.common.eventbus.Subscribe;
import com.mdt.ait.AIT;
import com.mdt.ait.client.renderers.layers.CowSkullModelLayer;
import com.mdt.ait.common.blocks.ConsoleBlock;
import com.mdt.ait.common.blocks.GBTCasingBlock;
import com.mdt.ait.common.blocks.RampBlock;
import com.mdt.ait.common.blocks.TardisCoralBlock;
import com.mdt.ait.common.tileentities.ConsoleTileEntity;
import com.mdt.ait.common.tileentities.TypewriterTile;
import com.mdt.ait.core.init.AITDimensionTypes;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.core.init.AITItems;
import com.mdt.ait.core.init.interfaces.ICantBreak;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.UUID;
import java.util.logging.Level;

public class CommonEventHandler {

    public int run_once = 1;
    public int run_once_cancelling = 1;
    public int x_number;
    public int z_number;

    @SubscribeEvent
    public void onPlayerMine(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() instanceof ICantBreak) {
            event.setCanceled(true);
        }
    }

    //@SubscribeEvent
    //public void onBlockBreak(BlockEvent.BreakEvent event) {
    //    BlockPos blockPos = event.getPos();
    //    TileEntity tileEntity = event.getWorld().getBlockEntity(blockPos);
    //    if (event.getState().getBlock() instanceof ConsoleBlock) {
    //        ConsoleTileEntity consoleTileEntity = (ConsoleTileEntity) tileEntity;
    //        if(consoleTileEntity.isRemovable) {
    //            event.setCanceled(false);
    //        } else {
    //            event.setCanceled(true);
    //        }
    //    }
    //}

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) { // When world loads
        if (!event.getWorld().isClientSide()) { // Server Side Only
            ServerWorld world = (ServerWorld) event.getWorld();
            assert world != null;
            if (world.dimension().equals(ServerWorld.OVERWORLD)) {

                AIT.server = ServerLifecycleHooks.getCurrentServer();
                AIT.dimensionSavedDataManager = AIT.server.overworld().getDataStorage();
            }
        }

    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getState().getBlock() instanceof TardisCoralBlock) {
            BlockPos blockPos = event.getPos();
            BlockPos casingPos1 = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1);
            BlockPos casingPos2 = new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ());
            BlockPos casingPos3 = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1);
            BlockPos casingPos4 = new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ());
            BlockPos casingPos = casingPos1;
            Entity entity = event.getEntity();
            PlayerEntity p = (PlayerEntity) entity;
            BlockState blockState = event.getPlacedBlock();
            Block block = blockState.getBlock();
            if(blockState.getBlockState().getValue(TardisCoralBlock.FACING) == Direction.SOUTH) {
                casingPos = casingPos1;
            }
            if(blockState.getBlockState().getValue(TardisCoralBlock.FACING) == Direction.WEST) {
                casingPos = casingPos2;
            }
            if(blockState.getBlockState().getValue(TardisCoralBlock.FACING) == Direction.NORTH) {
                casingPos = casingPos3;
            }
            if(blockState.getBlockState().getValue(TardisCoralBlock.FACING) == Direction.EAST) {
                casingPos = casingPos4;
            }
            BlockPos bPos = new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ());
            BlockPos bPos1 = new BlockPos(blockPos.getX() + 1, blockPos.getY() - 1, blockPos.getZ());
            BlockPos bPos2 = new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ());
            BlockPos bPos3 = new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() + 1);
            BlockPos bPos4 = new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() - 1);
            BlockPos bPos5 = new BlockPos(blockPos.getX() + 1, blockPos.getY() - 1, blockPos.getZ() + 1);
            BlockPos bPos6 = new BlockPos(blockPos.getX() + 1, blockPos.getY() - 1, blockPos.getZ() - 1);
            BlockPos bPos7 = new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() + 1);
            BlockPos bPos8 = new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1);
            IWorld world = event.getWorld();
            if (world.getBlockState(bPos).getBlock() instanceof SoulSandBlock
                    && (world.getBlockState(bPos1).getBlock() instanceof SoulSandBlock)
                    && (world.getBlockState(bPos2).getBlock() instanceof SoulSandBlock)
                    && (world.getBlockState(bPos3).getBlock() instanceof SoulSandBlock)
                    && (world.getBlockState(bPos4).getBlock() instanceof SoulSandBlock)
                    && (world.getBlockState(bPos5).getBlock() instanceof SoulSandBlock)
                    && (world.getBlockState(bPos6).getBlock() instanceof SoulSandBlock)
                    && (world.getBlockState(bPos7).getBlock() instanceof SoulSandBlock)
                    && (world.getBlockState(bPos8).getBlock() instanceof SoulSandBlock)
                    //&& (world.getBlockState(casingPos).getBlock() instanceof GBTCasingBlock)
                    && world.canSeeSky(blockPos) && world != AITDimensions.TARDIS_DIMENSION) {
                event.setCanceled(false);
            } else {
                event.setCanceled(true);
                if(world == AITDimensions.TARDIS_DIMENSION) {
                    p.sendMessage(new TranslationTextComponent("You can't grow a TARDIS in a TARDIS!"), UUID.randomUUID());
                } else {
                    p.sendMessage(new TranslationTextComponent("You can't grow this on here!"), UUID.randomUUID());
                }
            }
        }
    }

    //@SubscribeEvent
    //public void onEntityRender(RenderPlayerEvent event) {
    //    event.setCanceled(true);
    //}

    /*@SubscribeEvent
    public void doClientStuff(FMLClientSetupEvent event) {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager manager = mc.getEntityRenderDispatcher();
        PlayerRenderer playerRenderer = ;
        playerRenderer.addLayer(new CowSkullModelLayer<>(playerRenderer));

    }*/
}
