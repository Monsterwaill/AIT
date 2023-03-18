package com.mdt.ait.common.tileentities;

import com.mdt.ait.core.init.AITTiles;
import com.mdt.ait.core.init.enums.EnumRotorState;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class HartnellRotorTile extends TileEntity implements ITickableTileEntity {

    public UUID tardisID;
    public float rotorTick;
    public float setRotorTick = 0;
    private final int __dontUse = 0;
    public boolean isLoaded = false;
    public boolean isInFlight;
    public EnumRotorState currentstate = EnumRotorState.STATIC;
    public float spinny = 0;

    public EnumRotorState currentState() {
        return currentstate;
    }

    public HartnellRotorTile() {
        super(AITTiles.HARTNELL_ROTOR_TILE_ENTITY_TYPE.get());
    }

    public void getFlightState() {
        // Tardis tardis = AIT.tardisManager.getTardis(tardisID);
        // ServerWorld world = AIT.server.getExteriorLevel(tardis.exterior_dimension);
        // assert world != null;
        // TardisTileEntity tardisTileEntity = (TardisTileEntity)
        // world.getBlockEntity(tardis.exterior_position);
        // assert tardisTileEntity != null;
        // if(tardisTileEntity.getMatState() != EnumMatState.SOLID) {
        // if(rotorTick >= 0) {
        // rotorTick -= 1;
        // } else {
        // rotorTick = setRotorTick;
        // }
        // if(rotorTick <= -10) {
        // rotorTick += 1;
        // }
    }

    @Override
    public void tick() {
        if (this.tardisID != null) {
            if (this.getLevel() != null) {
                if (!this.getLevel().isClientSide()) {
                    /*
                     * Tardis tardis = AIT.tardisManager.getTardis(tardisID); if(!tardis.landed) {
                     * isInFlight = true; } else { isInFlight = false; }
                     */
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
        super.load(pState, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        if (this.tardisID != null) {
            nbt.putUUID("tardisID", this.tardisID);
        }
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
}
