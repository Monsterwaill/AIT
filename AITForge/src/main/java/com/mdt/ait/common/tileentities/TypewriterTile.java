package com.mdt.ait.common.tileentities;

import com.mdt.ait.common.blocks.TARDISBlock;
import com.mdt.ait.core.init.AITTiles;
import com.mdt.ait.network.depreciated.Network;
import com.mdt.ait.network.depreciated.packets.MonitorExteriorChangePacket;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TypewriterTile extends TileEntity {

    public UUID tardisID;

    public TypewriterTile() {
        super(AITTiles.TYPEWRITER_TILE_ENTITY_TYPE.get());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition).inflate(10, 10, 10);
    }

    public void setLastExterior() {
        World world = this.getLevel();
        BlockPos bpos = new BlockPos(
                this.getBlockPos().getX() + 1,
                this.getBlockPos().getY(),
                this.getBlockPos().getZ());
        Block tardisBlock = world.getBlockState(bpos).getBlock();
        if (tardisBlock instanceof TARDISBlock) {
            TileEntity tileEntity = world.getBlockEntity(bpos).getTileEntity();
            // ((TARDISTileEntity) tileEntity).lastExteriorFromMonitor();
        }
        Network.sendToServer(new MonitorExteriorChangePacket(11));
    }

    public void setNextExterior() {
        World world = this.getLevel();
        BlockPos bpos = new BlockPos(
                this.getBlockPos().getX() + 1,
                this.getBlockPos().getY(),
                this.getBlockPos().getZ());
        Block tardisBlock = world.getBlockState(bpos).getBlock();
        if (tardisBlock instanceof TARDISBlock) {
            TileEntity tileEntity = world.getBlockEntity(bpos).getTileEntity();
            // ((TARDISTileEntity) tileEntity).lastExteriorFromMonitor();
        }
        Network.sendToServer(new MonitorExteriorChangePacket(11));
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        if (tardisID != null) {
            compound.putUUID("tardisID", this.tardisID);
        }
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if (tardisID != null) {
            this.tardisID = nbt.getUUID("tardisID");
        }
        super.load(state, nbt);
    }
}
