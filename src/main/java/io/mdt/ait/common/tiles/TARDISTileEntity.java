package io.mdt.ait.common.tiles;

import com.mdt.ait.common.blocks.BasicInteriorDoorBlock;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.core.init.AITTiles;
import com.qouteall.immersive_portals.api.PortalAPI;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.TARDISManager;
import io.mdt.ait.tardis.door.TARDISDoorState;
import io.mdt.ait.tardis.link.impl.TARDISLinkableTileEntity;
import io.mdt.ait.tardis.portal.Portal3i;
import io.mdt.ait.util.TARDISUtil;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.ForgeChunkManager;

public class TARDISTileEntity extends TARDISLinkableTileEntity {

    public TARDISTileEntity() {
        this(AITTiles.TARDIS_TILE_ENTITY_TYPE.get());
    }

    public TARDISTileEntity(TileEntityType entity) {
        super(entity);
    }

    @Override
    public void onLoad() {
        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            this.link(TARDISManager.getInstance()
                    .create(this.getBlockPos(), this.getLevel().dimension()));
        }
    }

    @Override
    public void link(TARDIS tardis) {
        super.link(tardis);

        if (this.getLevel() != null
                && !this.getLevel().isClientSide()
                && this.getDoor().getPortal() == null) {
            this.spawnPortal();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.worldPosition).inflate(1500, 1500, 1500);
    }

    @SuppressWarnings("DataFlowIssue")
    public void spawnPortal() {
        Portal3i portal3i = this.getExterior().getSchema().portal();
        BlockPos doorPos = this.getDoor().getDoorPosition();
        ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
        Direction doorFacing = this.getDoor().getTile().getFacing();

        ForgeChunkManager.forceChunk(
                (ServerWorld) this.getLevel(), "ait", this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
        ForgeChunkManager.forceChunk(TARDISUtil.getTARDISWorld(), "ait", doorPos, chunkPos.x, chunkPos.z, true, true);
        this.sync();

        Portal portal = Portal.entityType.create(this.getLevel());

        portal.setOrientationAndSize(
                new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D), portal3i.width(), portal3i.height());

        Quaternion finalQuaternion = null;
        Quaternion quaternion = null;
        Vector3d destination = null;
        Vector3d origin = null;

        switch (doorFacing) {
            case NORTH:
                destination =
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.865D);
                break;
            case SOUTH:
                destination =
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.135D);
                break;
            case EAST:
                destination =
                        new Vector3d(doorPos.getX() + 0.135D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D);
                break;
            case WEST:
                destination =
                        new Vector3d(doorPos.getX() + 0.865D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D);
                break;
        }

        if (this.getFacing() == Direction.NORTH) {
            finalQuaternion = new Quaternion(Vector3f.YN, 180, true);

            if (doorFacing == Direction.NORTH) {
                quaternion = new Quaternion(Vector3f.YP, 180, true);
            }
            if (doorFacing == Direction.SOUTH) {
                quaternion = new Quaternion(Vector3f.YP, 0, true);
            }
            if (doorFacing == Direction.EAST) {
                quaternion = new Quaternion(Vector3f.YP, 90, true);
            }
            if (doorFacing == Direction.WEST) {
                quaternion = new Quaternion(Vector3f.YP, -90, true);
            }

            origin = new Vector3d(
                    this.getBlockPos().getX() + portal3i.x(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() - portal3i.z());
        }
        if (this.getFacing() == Direction.SOUTH) {
            finalQuaternion = new Quaternion(Vector3f.YN, 0, true);

            if (doorFacing == Direction.NORTH) {
                quaternion = new Quaternion(Vector3f.YP, 0, true);
            }
            if (doorFacing == Direction.SOUTH) {
                quaternion = new Quaternion(Vector3f.YP, 180, true);
            }
            if (doorFacing == Direction.EAST) {
                quaternion = new Quaternion(Vector3f.YP, -90, true);
            }
            if (doorFacing == Direction.WEST) {
                quaternion = new Quaternion(Vector3f.YP, 90, true);
            }

            origin = new Vector3d(
                    this.getBlockPos().getX() + portal3i.x(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + 1 + portal3i.z());
        }
        if (this.getFacing() == Direction.EAST) {
            finalQuaternion = new Quaternion(Vector3f.YN, -90, true);

            if (doorFacing == Direction.NORTH) {
                quaternion = new Quaternion(Vector3f.YP, -90, true);
            }
            if (doorFacing == Direction.SOUTH) {
                quaternion = new Quaternion(Vector3f.YP, 90, true);
            }
            if (doorFacing == Direction.EAST) {
                quaternion = new Quaternion(Vector3f.YP, 180, true);
            }
            if (doorFacing == Direction.WEST) {
                quaternion = new Quaternion(Vector3f.YP, 0, true);
            }

            origin = new Vector3d(
                    this.getBlockPos().getX() + 1 + portal3i.z(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + portal3i.x());
        }
        if (this.getFacing() == Direction.WEST) {
            finalQuaternion = new Quaternion(Vector3f.YN, 90, true);

            if (doorFacing == Direction.NORTH) {
                quaternion = new Quaternion(Vector3f.YP, 90, true);
            }
            if (doorFacing == Direction.SOUTH) {
                quaternion = new Quaternion(Vector3f.YP, -90, true);
            }
            if (doorFacing == Direction.EAST) {
                quaternion = new Quaternion(Vector3f.YP, 0, true);
            }
            if (doorFacing == Direction.WEST) {
                quaternion = new Quaternion(Vector3f.YP, 180, true);
            }

            origin = new Vector3d(
                    this.getBlockPos().getX() - portal3i.z(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + portal3i.x());
        }

        PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, destination, quaternion, 1d);
        PortalManipulation.rotatePortalBody(portal, finalQuaternion);

        portal.setOriginPos(origin);

        this.sync();
        PortalAPI.spawnServerEntity(portal);

        portal = PortalManipulation.completeBiWayPortal(portal, Portal.entityType);
        portal.reloadAndSyncToClient();
    }

    public Direction getFacing() {
        return this.getBlockState().getValue(BasicInteriorDoorBlock.FACING);
    }

    public void setFacing(Direction direction) {
        this.getBlockState().setValue(BasicInteriorDoorBlock.FACING, direction);
        this.sync();
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 0, this.save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.load(this.getBlockState(), packet.getTag());
    }

    public ActionResultType use(PlayerEntity player, BlockPos pos) {
        if (this.getLevel() == null || this.getLevel().isClientSide()) return ActionResultType.FAIL;

        TARDISDoorState state = this.getDoor().nextState(); // even if the door is locked, next state will return LOCKED
        this.getLevel().playSound(null, pos, state.sound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (this.getDoor().getState() == TARDISDoorState.LOCKED)
            player.displayClientMessage(
                    new TranslationTextComponent("Door is locked!")
                            .setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)),
                    true);

        // sync stuff
        this.getDoor().getTile().sync();
        this.sync();

        return ActionResultType.SUCCESS;
    }
}
