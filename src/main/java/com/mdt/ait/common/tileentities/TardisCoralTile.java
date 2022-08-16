package com.mdt.ait.common.tileentities;

import com.mdt.ait.AIT;
import com.mdt.ait.common.blocks.TardisCoralBlock;
import com.mdt.ait.core.init.AITBlocks;
import com.mdt.ait.core.init.AITSounds;
import com.mdt.ait.core.init.AITTiles;
import com.mdt.ait.core.init.enums.EnumCoralState;
import com.mdt.ait.core.init.enums.EnumExteriorType;
import com.mdt.ait.core.init.enums.EnumMatState;
import com.mdt.ait.tardis.Tardis;
import com.mdt.ait.tardis.TardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Random;
import java.util.UUID;

public class TardisCoralTile extends TileEntity implements ITickableTileEntity {

    protected EnumCoralState coralState = EnumCoralState.FIRST;
    private int ticks, pulses;
    private int run_once = 0;
    public TardisTileEntity tte;
    public Direction facingDirection = Direction.NORTH;

    public TardisCoralTile() {
        super(AITTiles.TARDIS_CORAL_TILE_ENTITY_TYPE.get());
    }

    public EnumCoralState getNextCoralState() {
        switch (coralState) {
            case FIRST:
                return EnumCoralState.SECOND;
            case SECOND:
                return EnumCoralState.THIRD;
            case THIRD:
                return EnumCoralState.FOURTH;
            case FOURTH:
                return EnumCoralState.FIFTH;
            case FIFTH:
                return EnumCoralState.SIXTH;
            case SIXTH:
                return EnumCoralState.SEVENTH;
            case SEVENTH:
                return EnumCoralState.FINAL;
            case FINAL:
                return EnumCoralState.FIRST;
        }
        return EnumCoralState.FIRST;
    }

    public EnumCoralState getCoralState() {
        return this.coralState;
    }

    @Override
    public void tick() {
        TardisManager tardisManager = AIT.tardisManager;
        TileEntity entity = level.getBlockEntity(worldPosition);
        ++ticks;
        if(ticks == 37.5/*750*/) {
            this.coralState = getNextCoralState();
            level.playSound(null, worldPosition, SoundType.BAMBOO.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        if(ticks == 75/*1500*/) {
            this.coralState = getNextCoralState();
            level.playSound(null, worldPosition, SoundType.BAMBOO.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        if(ticks == 112.5/*2250*/) {
            this.coralState = getNextCoralState();
            level.playSound(null, worldPosition, SoundType.BAMBOO.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        if(ticks == 150/*3000*/) {
            this.coralState = getNextCoralState();
            level.playSound(null, worldPosition, SoundType.BAMBOO.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        if(ticks == 187.5/*3750*/) {
            this.coralState = getNextCoralState();
            level.playSound(null, worldPosition, SoundType.BAMBOO.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        if(ticks == 225/*4500*/) {
            this.coralState = getNextCoralState();
            level.playSound(null, worldPosition, SoundType.BAMBOO.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        if(ticks == 262.5/*5250*/) {
            this.coralState = getNextCoralState();
            level.playSound(null, worldPosition, SoundType.BAMBOO.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        if (ticks >= 300/*6000*/) {  //6000 is 5 minutes
            if (!level.isClientSide) {
                switchDirectionForTARDIS();
                if(run_once == 0) {
                    try {
                        Tardis tardis = tardisManager.createTardis(UUID.randomUUID(), worldPosition, entity.getLevel().dimension());
                        TardisTileEntity tardisTileEntity = (TardisTileEntity) level.getBlockEntity(worldPosition);
                        assert tardisTileEntity != null;
                        tardisTileEntity.linked_tardis = tardis;
                        tardisTileEntity.linked_tardis_id = tardis.tardisID;
                        tardisTileEntity.matState = EnumMatState.REMAT;
                        tardis.exterior_facing = facingDirection;
                        tardisTileEntity.currentexterior = pickRandomExterior();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    run_once = 1;
                }
            }
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition).inflate(10, 10, 10);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("coralState", this.coralState.ordinal());
        return super.save(nbt);
    }

    @Override
    public void load(BlockState pState, CompoundNBT nbt) {
        this.coralState = EnumCoralState.values()[nbt.getInt("coralState")];
        super.load(pState, nbt);
    }

    public boolean switchDirectionForTARDIS() {
        BlockState blockState = level.getBlockState(worldPosition);
        TileEntity entity = level.getBlockEntity(worldPosition);
        switch (blockState.getValue(TardisCoralBlock.FACING)) {
            case NORTH:
                return level.setBlock(worldPosition, AITBlocks.TARDIS_BLOCK.get().defaultBlockState().rotate(entity.getLevel(), worldPosition, Rotation.NONE), 512);
            case EAST:
                return level.setBlock(worldPosition, AITBlocks.TARDIS_BLOCK.get().defaultBlockState().rotate(entity.getLevel(), worldPosition, Rotation.CLOCKWISE_90), 512);
            case SOUTH:
                return level.setBlock(worldPosition, AITBlocks.TARDIS_BLOCK.get().defaultBlockState().rotate(entity.getLevel(), worldPosition, Rotation.CLOCKWISE_180), 512);
            case WEST:
                return level.setBlock(worldPosition, AITBlocks.TARDIS_BLOCK.get().defaultBlockState().rotate(entity.getLevel(), worldPosition, Rotation.COUNTERCLOCKWISE_90), 512);
            default:
                throw new RuntimeException("Invalid facing direction in getCollisionShape() " +
                        "//HOW THE HECK DID YOU GET HERE??");
        }
    }

    public Direction exteriorFacingCoralBlock() {
        if(TardisCoralBlock.FACING.equals(Direction.NORTH)) {
            return facingDirection = Direction.NORTH;
        }
        if(TardisCoralBlock.FACING.equals(Direction.EAST)) {
            return facingDirection = Direction.EAST;
        }
        if(TardisCoralBlock.FACING.equals(Direction.SOUTH)) {
            return facingDirection = Direction.SOUTH;
        }
        if(TardisCoralBlock.FACING.equals(Direction.WEST)) {
            return facingDirection = Direction.WEST;
        }
        return facingDirection;
    }

    public EnumExteriorType pickRandomExterior() {
        return EnumExteriorType.values()[new Random().nextInt(EnumExteriorType.values().length)];
    }
}
