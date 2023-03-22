package io.mdt.ait.common.tiles;

import com.mdt.ait.common.blocks.BasicInteriorDoorBlock;
import com.mdt.ait.common.blocks.TARDISBlock;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.core.init.AITSounds;
import com.mdt.ait.core.init.AITTiles;
import com.qouteall.immersive_portals.api.PortalAPI;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.link.impl.TARDISLinkableTileEntity;
import io.mdt.ait.tardis.portal.Portal3i;
import io.mdt.ait.util.TARDISUtil;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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
import net.minecraft.world.World;
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
    public void link(TARDIS tardis) {
        System.out.println("Linking...");
        super.link(tardis);
        System.out.println("Linked!");

        if (this.getLevel() != null
                && !this.getLevel().isClientSide
                && !this.getDoor().getPortal().isBuilt()) {
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
                        this.getDoor().getDoorPosition().getZ() + 0.865D);

                break;

            case SOUTH:
                position = new Vector3d(
                        this.getDoor().getDoorPosition().getX() + 0.5D,
                        this.getDoor().getDoorPosition().getY() + info.y(),
                        this.getDoor().getDoorPosition().getZ() + 0.135D);

                break;

            case EAST:
                position = new Vector3d(
                        this.getDoor().getDoorPosition().getX() + 0.135D,
                        this.getDoor().getDoorPosition().getY() + info.y(),
                        this.getDoor().getDoorPosition().getZ() + 0.5D);

                break;

            case WEST:
                position = new Vector3d(
                        this.getDoor().getDoorPosition().getX() + 0.865D,
                        this.getDoor().getDoorPosition().getY() + info.y(),
                        this.getDoor().getDoorPosition().getZ() + 0.5D);

                break;
        }

        return position;
    }

    private final float[][] portalRotation = {
        {180.0F, 0.0F, 90.0F, -90.0F},
        {0.0F, 180.0F, -90.0F, 90.0F},
        {90.0F, -90.0F, 0.0F, 180.0F},
        {-90.0F, 90.0F, 180.0F, 0.0F}
    };

    public void spawnPortal() {
        Portal portal;
        BlockPos doorPos = this.getDoor().getDoorPosition();
        ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
        Direction doorFacing = this.getDoor().getTile().getFacing();

        ForgeChunkManager.forceChunk(
                (ServerWorld) this.getLevel(), "ait", this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
        ForgeChunkManager.forceChunk(TARDISUtil.getTARDISWorld(), "ait", doorPos, chunkPos.x, chunkPos.z, true, true);
        this.sync();

        portal = Portal.entityType.create(this.getLevel());
        portal.setOriginPos(new Vector3d(
                this.getBlockPos().getX() + 0.5D,
                this.getBlockPos().getY(),
                (this.getBlockPos().getZ() + 1)));

        Portal3i portal3i = this.getExterior().getSchema().portal();
        portal.setOrientationAndSize(
                new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D), portal3i.width(), portal3i.height());

        if (this.getFacing() == Direction.NORTH) {
            if (doorFacing == Direction.NORTH) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.865D),
                        new Quaternion(Vector3f.YP, 180.0F, true),
                        1d);
            }
            if (doorFacing == Direction.SOUTH) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.135D),
                        new Quaternion(Vector3f.YP, 0.0F, true),
                        1d);
            }
            if (doorFacing == Direction.EAST) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.135D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D),
                        new Quaternion(Vector3f.YP, 90.0F, true),
                        1d);
            }
            if (doorFacing == Direction.WEST) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.865D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D),
                        new Quaternion(Vector3f.YP, -90.0F, true),
                        1d);
            }

            PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 180, true));
            portal.setOriginPos(new Vector3d(
                    this.getBlockPos().getX() + portal3i.x(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() - portal3i.z()));
        }
        if (this.getFacing() == Direction.SOUTH) {
            if (doorFacing == Direction.NORTH) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.865D),
                        new Quaternion(Vector3f.YP, 0.0F, true),
                        1d);
            }
            if (doorFacing == Direction.SOUTH) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.135D),
                        new Quaternion(Vector3f.YP, 180.0F, true),
                        1d);
            }
            if (doorFacing == Direction.EAST) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.135D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D),
                        new Quaternion(Vector3f.YP, -90.0F, true),
                        1d);
            }
            if (doorFacing == Direction.WEST) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.865D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D),
                        new Quaternion(Vector3f.YP, 90.0F, true),
                        1d);
            }

            PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 0, true));
            portal.setOriginPos(new Vector3d(
                    this.getBlockPos().getX() + portal3i.x(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + 1 + portal3i.z()));
        }
        if (this.getFacing() == Direction.EAST) {
            if (doorFacing == Direction.NORTH) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.865D),
                        new Quaternion(Vector3f.YP, -90.0F, true),
                        1d);
            }
            if (doorFacing == Direction.SOUTH) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.135D),
                        new Quaternion(Vector3f.YP, 90.0F, true),
                        1d);
            }
            if (doorFacing == Direction.EAST) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.135D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D),
                        new Quaternion(Vector3f.YP, 180.0F, true),
                        1d);
            }
            if (doorFacing == Direction.WEST) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.235D),
                        new Quaternion(Vector3f.YP, 0.0F, true),
                        1d);
            }

            PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, -90, true));
            portal.setOriginPos(new Vector3d(
                    this.getBlockPos().getX() + 1 + portal3i.z(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + portal3i.x()));
        }
        if (this.getFacing() == Direction.WEST) {
            if (doorFacing == Direction.NORTH) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.865D),
                        new Quaternion(Vector3f.YP, 90.0F, true),
                        1d);
            }
            if (doorFacing == Direction.SOUTH) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.5D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.135D),
                        new Quaternion(Vector3f.YP, -90.0F, true),
                        1d);
            }
            if (doorFacing == Direction.EAST) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.135D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D),
                        new Quaternion(Vector3f.YP, 0.0F, true),
                        1d);
            }
            if (doorFacing == Direction.WEST) {
                PortalManipulation.setPortalTransformation(
                        portal,
                        AITDimensions.TARDIS_DIMENSION,
                        new Vector3d(doorPos.getX() + 0.865D, doorPos.getY() + portal3i.y(), doorPos.getZ() + 0.5D),
                        new Quaternion(Vector3f.YP, 180.0F, true),
                        1d);
            }

            PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 90, true));
            portal.setOriginPos(new Vector3d(
                    this.getBlockPos().getX() - portal3i.z(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + portal3i.x()));
        }

        this.sync();

        portal.setDestinationDimension(AITDimensions.TARDIS_DIMENSION);
        PortalAPI.spawnServerEntity(portal);

        portal = PortalManipulation.completeBiWayPortal(portal, Portal.entityType);
        portal.reloadAndSyncToClient();
        // end TTE
        // start door
        // portal.setDestination(new Vector3d(
        //        this.getBlockPos().getX(),
        //        this.getBlockPos().getY(),
        //        this.getBlockPos().getZ()));

        // ...

        /*ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
        ForgeChunkManager.forceChunk(
                (ServerWorld) TARDISUtil.getWorld(this.level.dimension()),
                "ait",
                this.getBlockPos(),
                chunkPos.x,
                chunkPos.z,
                true,
                true);
        ForgeChunkManager.forceChunk(
                TARDISUtil.getTARDISWorld(),
                "ait",
                this.getDoor().getDoorPosition(),
                chunkPos.x,
                chunkPos.z,
                true,
                true);

        this.sync();
        TARDISDoor door = this.getDoor();
        TARDISInteriorDoorTile doorTile = door.getTile();

        Vector3d vector3d;
        Portal3i portal3i = this.getExterior().getSchema().portal();
        if (this.getFacing() == Direction.NORTH) {
            vector3d = new Vector3d(
                    this.getBlockPos().getX() + portal3i.x(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() - portal3i.z());
        } else if (this.getFacing() == Direction.SOUTH) {
            vector3d = new Vector3d(
                    this.getBlockPos().getX() + portal3i.x(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + 1 + portal3i.z());
        } else if (this.getFacing() == Direction.WEST) {
            vector3d = new Vector3d(
                    this.getBlockPos().getX() - portal3i.z(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + portal3i.x());
        } else {
            vector3d = new Vector3d(
                    this.getBlockPos().getX() + 1 + portal3i.z(),
                    this.getBlockPos().getY() + portal3i.y(),
                    this.getBlockPos().getZ() + portal3i.x());
        }

        new TARDISPortal(this.getLevel())
                .from(vector3d)
                .to(AITDimensions.TARDIS_DIMENSION, this.getVectorByDirection(doorTile.getFacing(), portal3i))
                .withOrientationAndSize(new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D), portal3i)
                .setRotation(new Quaternion(
                        Vector3f.YP,
                        this.portalRotation[this.getFacing().ordinal() - 2][
                                doorTile.getFacing().ordinal() - 2],
                        true))
                .build();
        System.out.println(this.getDoor().getDoorPosition());
        doorTile.sync();
        this.sync();*/
    }

    public Direction getFacing() {
        return this.getBlockState().getValue(BasicInteriorDoorBlock.FACING);
    }

    public void setFacing(Direction direction) {
        this.getBlockState().setValue(BasicInteriorDoorBlock.FACING, direction);
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

    public ActionResultType use(World world, PlayerEntity player, BlockPos pos, Hand hand) {
        if (!world.isClientSide) {
            BlockState blockstate = world.getBlockState(pos);
            Block block = blockstate.getBlock();
            if (!this.getDoor().getState().isLocked()) {
                if (block instanceof TARDISBlock && hand == Hand.MAIN_HAND) {
                    this.getDoor().getState().next();

                    // if (doorPos != null) {
                    //    this.linked_tardis.setInteriorDoorState(this.currentstate);
                    // }

                    this.getDoor().getTile().sync();

                    switch (this.getDoor().getState().get()) {
                        case FIRST:
                        case BOTH:
                            world.playSound(
                                    null, pos, AITSounds.POLICE_BOX_OPEN.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                            break;

                        case CLOSED:
                            world.playSound(
                                    null, pos, AITSounds.POLICE_BOX_CLOSE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }

                    this.sync();
                }
            } else {
                player.displayClientMessage(
                        new TranslationTextComponent("Door is locked!")
                                .setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)),
                        true);
                this.level.playSound(null, pos, AITSounds.TARDIS_LOCK.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.sync();
            }
        }

        return ActionResultType.SUCCESS;
    }
}
