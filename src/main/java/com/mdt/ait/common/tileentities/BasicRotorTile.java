package com.mdt.ait.common.tileentities;

import com.mdt.ait.AIT;
import com.mdt.ait.client.models.tileentities.controls.BasicRotor;
import com.mdt.ait.common.blocks.BasicRotorBlock;
import com.mdt.ait.common.blocks.TardisLeverBlock;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.core.init.AITSounds;
import com.mdt.ait.core.init.AITTiles;
import com.mdt.ait.core.init.enums.EnumLeverState;
import com.mdt.ait.core.init.enums.EnumMatState;
import com.mdt.ait.core.init.enums.EnumRotorState;
import com.mdt.ait.tardis.Tardis;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.UUID;

public class BasicRotorTile extends TileEntity implements ITickableTileEntity {

    public UUID tardisID;
    public float rotorTick;
    public float setRotorTick = 0;
    private int __dontUse = 0;
    public boolean isLoaded = false;
    public EnumRotorState currentstate = EnumRotorState.STATIC;
    public float spinny = 0;

    public EnumRotorState currentState() {
        return currentstate;
    }

    public BasicRotorTile() {
        super(AITTiles.BASIC_ROTOR_TILE_ENTITY_TYPE.get());
    }

    public void getFlightState() {
        //Tardis tardis = AIT.tardisManager.getTardis(tardisID);
        //ServerWorld world = AIT.server.getLevel(tardis.exterior_dimension);
        //assert world != null;
        //TardisTileEntity tardisTileEntity = (TardisTileEntity) world.getBlockEntity(tardis.exterior_position);
        //assert tardisTileEntity != null;
        //if(tardisTileEntity.getMatState() != EnumMatState.SOLID) {
        //if(rotorTick >= 0) {
        //        rotorTick -= 1;
        //} else {
        //    rotorTick = setRotorTick;
        //}
        //if(rotorTick <= -10) {
        //    rotorTick += 1;
        //}
    }

    /*public boolean isTardisInFlight() {
        boolean isFlight = false;
        Tardis tardis = AIT.tardisManager.getTardis(tardisID);
        ServerWorld exteriorWorld = AIT.server.getLevel(tardis.exterior_dimension);
        assert exteriorWorld != null;
        TardisTileEntity tardisTileEntity = (TardisTileEntity) exteriorWorld.getBlockEntity(tardis.exterior_position);
        if(tardisTileEntity.matState != EnumMatState.SOLID || tardisTileEntity == null) {
            isFlight = true;
        }
        return isFlight;
    }*/

    @Override
    public void tick() {

    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition).inflate(10, 10, 10);
    }

    @Override
    public void load(BlockState pState, CompoundNBT nbt) {
        this.tardisID = nbt.getUUID("tardisID");
        super.load(pState, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putUUID("tardisID", this.tardisID);
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
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        setChanged();
    }
}
