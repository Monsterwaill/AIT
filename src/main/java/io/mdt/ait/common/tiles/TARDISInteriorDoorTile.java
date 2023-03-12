package io.mdt.ait.common.tiles;

import com.mdt.ait.AIT;
import com.mdt.ait.common.blocks.BasicInteriorDoorBlock;
import com.mdt.ait.core.init.AITSounds;
import com.mdt.ait.core.init.AITTiles;
import io.mdt.ait.tardis.door.TARDISDoorState;
import io.mdt.ait.tardis.door.TARDISDoorStates;
import io.mdt.ait.tardis.link.impl.TARDISLinkableTileEntity;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.ForgeChunkManager;

public class TARDISInteriorDoorTile extends TARDISLinkableTileEntity implements ITickableTileEntity {

    private float rightDoorRotation = 0;
    private float leftDoorRotation = 0;

    public TARDISInteriorDoorTile() {
        super(AITTiles.BASIC_INTERIOR_DOOR_TILE_ENTITY_TYPE.get());
    }

    public float getLeftDoorRotation() {
        return this.leftDoorRotation;
    }

    public float getRightDoorRotation() {
        return this.rightDoorRotation;
    }

    public TARDISDoorState getState() {
        return this.getDoor().getState();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (this.getLevel() != null && !this.getLevel().isClientSide) {
            ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
            ForgeChunkManager.forceChunk((ServerWorld) this.getLevel(), AIT.MOD_ID, this.getBlockPos(), chunkPos.x, chunkPos.z, false, false);
        }

        this.sync();
    }

    TARDISDoorStates previousState = TARDISDoorStates.CLOSED; // TODO: replace

    @Override
    public void tick() {
        // remove portal if entity removed, probably can implement that in setRemoved()
        // ...


        if (this.isLinked() && this.getState() != null) {
            TARDISDoorStates state = this.getState().get();
            if (state != this.previousState) {
                this.rightDoorRotation = state == TARDISDoorStates.FIRST ? 0.0f : 87.5f;
                this.leftDoorRotation = state == TARDISDoorStates.FIRST ? 0.0f : (state == TARDISDoorStates.BOTH ? 0.0f : 87.5f);
            }
            if (state != TARDISDoorStates.CLOSED) {
                if (this.rightDoorRotation < 87.5f) {
                    this.rightDoorRotation += 5.0f;
                } else {
                    this.rightDoorRotation = 87.5f;
                }
            } else {
                if (this.leftDoorRotation > 0.0f && this.rightDoorRotation > 0.0f) {
                    this.leftDoorRotation -= 15.0f;
                    this.rightDoorRotation -= 15.0f;
                }
            }
            if (state == TARDISDoorStates.BOTH) {
                if (this.leftDoorRotation < 87.5f) {
                    this.leftDoorRotation += 5.0f;
                } else {
                    this.leftDoorRotation = 87.5f;
                }
            }
            if(state == TARDISDoorStates.CLOSED) {
                if(this.leftDoorRotation == -2.5f) {
                    this.leftDoorRotation = 0.0f;
                }
                if(this.rightDoorRotation == -2.5f) {
                    this.rightDoorRotation = 0.0f;
                }
            }

            this.previousState = state;
        }
    }

    public void use(World world, PlayerEntity player, BlockPos pos) {
        if (!world.isClientSide) {
            TARDISDoorStates state = this.getState().get();

            if (!this.getState().isLocked()) {
                state = this.getState().next();

                //TARDISUtil.getExteriorTile(this.getTARDIS()).sync();
            } else {
                player.displayClientMessage(new TranslationTextComponent(
                        "Door is locked!").setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)), true);
            }

            world.playSound(null, pos, state.getSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            this.sync();
        }
    }

    public void onKey(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();

        if (TARDISUtil.getExteriorTile(this.getTARDIS()) == null) {
            player.displayClientMessage(new TranslationTextComponent(
                    "TARDIS is in flight!").setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)), true);
            return;
        }

        if (!this.getTARDIS().ownsKey(context.getItemInHand())) {
            player.displayClientMessage(new TranslationTextComponent(
                    "This TARDIS is not yours!").setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)), true);
            return;
        }

        this.getState().setLocked(player.isCrouching());
        this.level.playSound(null, this.getDoor().getDoorPosition(), AITSounds.TARDIS_LOCK.get(), SoundCategory.MASTER, 1.0F, 1.0F);

        player.displayClientMessage(new TranslationTextComponent(
                this.getState().isLocked() ? "Door is locked!" : "Door is unlocked!").setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)), true);

        this.sync();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        this.leftDoorRotation = nbt.getFloat("left");
        this.rightDoorRotation = nbt.getFloat("right");

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putFloat("left", this.leftDoorRotation);
        nbt.putFloat("right", this.rightDoorRotation);

        return super.save(nbt);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.getBlockPos()).inflate(10, 10, 10);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), 0, this.save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.load(this.getBlockState(), packet.getTag());
    }

    public Direction getFacing() {
        return this.getBlockState().getValue(BasicInteriorDoorBlock.FACING);
    }
}
