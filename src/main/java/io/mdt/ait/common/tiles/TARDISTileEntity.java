package io.mdt.ait.common.tiles;

import com.mdt.ait.AIT;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.core.init.AITTiles;
import com.qouteall.immersive_portals.api.PortalAPI;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import io.mdt.ait.tardis.*;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.ForgeChunkManager;

import javax.annotation.Nonnull;

public class TARDISTileEntity extends TileEntity implements ITickableTileEntity, ITARDISLinked {

    private Portal portal;

    public TARDISTileEntity() {
        this(AITTiles.TARDIS_TILE_ENTITY_TYPE.get());
    }

    public TARDISTileEntity(TileEntityType entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (this.isLinked()) {
            this.spawnPortal();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.worldPosition).inflate(1500, 1500, 1500);
    }

    public void spawnPortal() {
        if (this.getLevel() != null && !this.getLevel().isClientSide) {
            ServerWorld tardisexteriordim = AIT.server.getLevel(this.getLevel().dimension());
            ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
            TARDISDoor door = this.getDoor().get();

            ForgeChunkManager.forceChunk(tardisexteriordim, "ait", this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
            if (door.getPosition() != null) {
                ForgeChunkManager.forceChunk(TARDISUtil.getTARDISWorld(), "ait", door.getPosition(), chunkPos.x, chunkPos.z, true, true);
            }

            this.syncToClient();

            TARDISInteriorDoorTile basicInteriorDoorTile = door.getTile();

            Portal portal = Portal.entityType.create(this.getLevel());
            portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY(), (this.getBlockPos().getZ() + 1)));

            PortalInfo portalInfo = this.getExterior().get().portal();


            portal.setOrientationAndSize(new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D),
                    portalInfo.width(), portalInfo.height());

            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.NORTH) {
                if (basicInteriorDoorTile.getFacing() == Direction.NORTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.865D), new Quaternion(Vector3f.YP, 180.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.SOUTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.135D), new Quaternion(Vector3f.YP, 0.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.EAST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.135D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.5D), new Quaternion(Vector3f.YP, 90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.WEST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.865D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.5D), new Quaternion(Vector3f.YP, -90.0F, true), 1d);
                }
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.SOUTH) {
                if (basicInteriorDoorTile.getFacing() == Direction.NORTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.865D), new Quaternion(Vector3f.YP, 0.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.SOUTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.135D), new Quaternion(Vector3f.YP, 180.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.EAST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.135D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.5D), new Quaternion(Vector3f.YP, -90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.WEST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.865D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.5D), new Quaternion(Vector3f.YP, 90.0F, true), 1d);
                }
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST) {
                if (basicInteriorDoorTile.getFacing() == Direction.NORTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.865D), new Quaternion(Vector3f.YP, -90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.SOUTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.135D), new Quaternion(Vector3f.YP, 90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.EAST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.135D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.5D), new Quaternion(Vector3f.YP, 180.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.WEST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.235D), new Quaternion(Vector3f.YP, 0.0F, true), 1d);
                }
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST) {
                if (basicInteriorDoorTile.getFacing() == Direction.NORTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.865D), new Quaternion(Vector3f.YP, 90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.SOUTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.5D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.135D), new Quaternion(Vector3f.YP, -90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.EAST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.135D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.5D), new Quaternion(Vector3f.YP, 0.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.WEST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            door.getPosition().getX() + 0.865D,
                            door.getPosition().getY() + portalInfo.y(),
                            door.getPosition().getZ() + 0.5D), new Quaternion(Vector3f.YP, 180.0F, true), 1d);
                }
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.NORTH) {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 180, true));
                portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + portalInfo.x(), this.getBlockPos().getY() + portalInfo.y(), this.getBlockPos().getZ() - portalInfo.z()));
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.SOUTH) {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 0, true));
                portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + portalInfo.x(), this.getBlockPos().getY() + portalInfo.y(), this.getBlockPos().getZ() + 1 + portalInfo.z()));
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST) {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, -90, true));
                portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + 1 + portalInfo.z(), this.getBlockPos().getY() + portalInfo.y(), this.getBlockPos().getZ() + portalInfo.x()));
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST) {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 90, true));
                portal.setOriginPos(new Vector3d(this.getBlockPos().getX() - portalInfo.z(), this.getBlockPos().getY() + portalInfo.y(), this.getBlockPos().getZ() + portalInfo.x()));
            }

            this.syncToClient();
            if (portal.getDestinationWorld() == null) {
                portal.setDestinationDimension(AITDimensions.TARDIS_DIMENSION);
            }

            PortalAPI.spawnServerEntity(portal);
            portal.reloadAndSyncToClient();
            this.portal = portal;

            door.getTile().setPortal(this.portal);
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        this.getLink().link(TARDISManager.findTARDIS(nbt.getUUID("tardis")));
        this.portal = (Portal) ((ServerWorld) this.getLevel()).getEntity(nbt.getUUID("portal"));

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        if (this.getTARDIS().isPresent()) {
            nbt.putUUID("tardis", this.getUUID().get());
            nbt.putUUID("portal", this.portal.getUUID());
        }

        return super.save(nbt);
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

    public void syncToClient() {
        if (this.level != null) {
            this.level.setBlocksDirty(this.worldPosition, this.level.getBlockState(this.worldPosition), this.level.getBlockState(this.worldPosition));
            this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(this.worldPosition), this.level.getBlockState(this.worldPosition), 3);
            this.setChanged();
        }
    }

    private final TARDISLink link = new TARDISLink();

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}