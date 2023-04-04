package com.mdt.ait.common.tileentities;

import com.mdt.ait.core.init.AITTiles;
import com.mdt.ait.tardis.special.DematTransit;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class ToyotaMonitorTile extends TileEntity implements ITickableTileEntity {

    public UUID tardisID;
    public String flightTimeRemaining = "0%";
    public DematTransit dematTransit;
    public int flightTicks = 0;
    public float monitorRotation;

    public ToyotaMonitorTile() {
        super(AITTiles.TOYOTA_MONITOR_TILE_ENTITY_TYPE.get());
    }

    @Override
    public void tick() {
        // System.out.println(this.monitorRotation);
        if (this.dematTransit != null) {
            if (this.dematTransit.readyForDemat) {
                this.flightTimeRemaining = (" " + (flightTicks * 100) / this.dematTransit.getFlightTicks() + "%");
                if (flightTicks == this.dematTransit.getFlightTicks()) {
                    flightTicks = 0;
                }
                if (flightTicks < this.dematTransit.getFlightTicks()) {
                    flightTicks += 1;
                }
            }
            if (this.dematTransit != null) {
                if (flightTicks >= this.dematTransit.getFlightTicks()) {
                    flightTicks = 0;
                    syncToClient();
                }
            }
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition).inflate(10, 10, 10);
    }

    @Override
    public void load(BlockState pState, CompoundNBT nbt) {
        if (nbt.contains("tardisID")) {
            this.tardisID = nbt.getUUID("tardisID");
        }
        this.flightTimeRemaining = nbt.getString("flightTime");
        this.monitorRotation = nbt.getFloat("monitorRotation");
        super.load(pState, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        if (this.tardisID != null) {
            nbt.putUUID("tardisID", this.tardisID);
        }
        nbt.putString("flightTime", this.flightTimeRemaining);
        nbt.putFloat("monitorRotation", this.monitorRotation);
        return super.save(nbt);
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 0, save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        load(getBlockState(), packet.getTag());
    }

    public void syncToClient() {
        assert level != null;
        level.setBlocksDirty(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition));
        level.sendBlockUpdated(
                worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        setChanged();
    }

    public void doRotation(
            BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        System.out.println(monitorRotation);
        monitorRotation += 22.5F;
    }
}
