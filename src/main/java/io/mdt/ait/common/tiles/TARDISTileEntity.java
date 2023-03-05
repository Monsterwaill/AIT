package io.mdt.ait.common.tiles;

import com.mdt.ait.common.blocks.BasicInteriorDoorBlock;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.core.init.AITTiles;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.TARDISManager;
import io.mdt.ait.tardis.link.impl.TARDISLinkableTileEntity;
import io.mdt.ait.tardis.portal.Portal3i;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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

public class TARDISTileEntity extends TARDISLinkableTileEntity {

    public TARDISTileEntity() {
        this(AITTiles.TARDIS_TILE_ENTITY_TYPE.get());
    }

    public TARDISTileEntity(TileEntityType entity) {
        super(entity);
    }

    @Override
    public void link(TARDIS tardis) {
        if (this.getLevel() != null && !this.getLevel().isClientSide && this.isLinked() && !this.getDoor().getPortal().isBuilt()) {
            this.spawnPortal();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.worldPosition).inflate(1500, 1500, 1500);
    }

    private Vector3d getVectorByDirection(Direction direction, Portal3i info) {
        Vector3d position = null;

        switch (direction) {
            case NORTH:
                position = new Vector3d(
                        this.getDoor().getDoorPosition().getX() + 0.5D,
                        this.getDoor().getDoorPosition().getY() + info.y(),
                        this.getDoor().getDoorPosition().getZ() + 0.865D
                );

                break;

            case SOUTH:
                position = new Vector3d(
                        this.getDoor().getDoorPosition().getX() + 0.5D,
                        this.getDoor().getDoorPosition().getY() + info.y(),
                        this.getDoor().getDoorPosition().getZ() + 0.135D
                );

                break;

            case EAST:
                position = new Vector3d(
                        this.getDoor().getDoorPosition().getX() + 0.135D,
                        this.getDoor().getDoorPosition().getY() + info.y(),
                        this.getDoor().getDoorPosition().getZ() + 0.5D
                );

                break;

            case WEST:
                position = new Vector3d(
                        this.getDoor().getDoorPosition().getX() + 0.865D,
                        this.getDoor().getDoorPosition().getY() + info.y(),
                        this.getDoor().getDoorPosition().getZ() + 0.5D
                );

                break;
        }

        return position;
    }

    private final float[][] portalRotation = {
            { 180.0F, 0.0F, 90.0F, -90.0F },
            { 0.0F, 180.0F, -90.0F, 90.0F },
            { 90.0F, -90.0F, 0.0F, 180.0F },
            { -90.0F, 90.0F, 180.0F, 0.0F }
    };

    public void spawnPortal() {
        Portal3i portal3i = this.getExterior().portal();
        ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
        TARDISInteriorDoorTile doorTile = this.getDoor().getTile();

        if (doorTile != null) {
            ForgeChunkManager.forceChunk((ServerWorld) TARDISUtil.getExteriorLevel(this.getTARDIS()), "ait", this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
            ForgeChunkManager.forceChunk(TARDISUtil.getTARDISWorld(), "ait", this.getDoor().getDoorPosition(), chunkPos.x, chunkPos.z, true, true);
            this.syncToClient();

            this.getDoor().getPortal()
                    .from(this.getLevel(), this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY(), this.getBlockPos().getZ() + 1, portal3i.width(), portal3i.height())
                    .to(
                            AITDimensions.TARDIS_DIMENSION, this.getVectorByDirection(doorTile.getFacing(), portal3i),
                            new Quaternion(
                                    Vector3f.YP,
                                    this.portalRotation
                                            [this.getFacing().ordinal() - 2]
                                            [doorTile.getFacing().ordinal() - 2],
                                    true
                            )
                    );

            this.getDoor().getPortal().apply(portal -> {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, this.portalRotation[this.getFacing().ordinal() - 2][0], true));
                if (this.getFacing() == Direction.NORTH) {
                    portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + portal3i.x(), this.getBlockPos().getY() + portal3i.y(), this.getBlockPos().getZ() - portal3i.z()));
                } else if (this.getFacing() == Direction.SOUTH) {
                    portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + portal3i.x(), this.getBlockPos().getY() + portal3i.y(), this.getBlockPos().getZ() + 1 + portal3i.z()));
                } else if (this.getFacing() == Direction.WEST) {
                    portal.setOriginPos(new Vector3d(this.getBlockPos().getX() - portal3i.z(), this.getBlockPos().getY() + portal3i.y(), this.getBlockPos().getZ() + portal3i.x()));
                } else {
                    portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + 1 + portal3i.z(), this.getBlockPos().getY() + portal3i.y(), this.getBlockPos().getZ() + portal3i.x()));
                }

                return portal;
            });

            this.syncToClient();
            this.getDoor().getPortal().build();
        }
    }

    public Direction getFacing() {
        return this.getBlockState().getValue(BasicInteriorDoorBlock.FACING);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        this.link(TARDISManager.getInstance().findTARDIS(nbt.getUUID("tardis")));
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        if (this.isLinked()) {
            nbt.putUUID("tardis", this.getUUID());
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
}
