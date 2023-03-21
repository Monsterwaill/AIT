package io.mdt.ait.common.tiles;

import com.mdt.ait.common.blocks.BasicInteriorDoorBlock;
import com.mdt.ait.common.blocks.TARDISBlock;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.core.init.AITSounds;
import com.mdt.ait.core.init.AITTiles;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.link.impl.TARDISLinkableTileEntity;
import io.mdt.ait.tardis.portal.Portal3i;
import io.mdt.ait.tardis.portal.TARDISPortal;
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
        /*Portal portal;
        ServerWorld exteriorWorld = TARDISUtil.getTARDISWorld();
        if (this.getLevel() != null &&
                !this.getLevel().isClientSide) {
            ServerWorld tardisinteriordim = AIT.server.getLevel(AITDimensions.TARDIS_DIMENSION);
            ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
            ForgeChunkManager.forceChunk(tardisexteriordim, "ait", this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
            if (this.linked_tardis.interior_door_position != null) {
                ForgeChunkManager.forceChunk(tardisinteriordim, "ait", this.linked_tardis.interior_door_position, chunkPos.x, chunkPos.z, true, true);
            }
        }
        this.sync();
        BasicInteriorDoorTile basicInteriorDoorTile = (BasicInteriorDoorTile) world.getBlockEntity(this.linked_tardis.interior_door_position);
        portal = Portal.entityType.create(this.getLevel());
        portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY(), (this.getBlockPos().getZ() + 1)));
        if (this.currentexterior == EnumExteriorType.BASIC_BOX
                || this.currentexterior == EnumExteriorType.MINT_BOX
                || this.currentexterior == EnumExteriorType.CORAL_BOX
                || this.currentexterior == EnumExteriorType.POSTER_BOX
                || this.currentexterior == EnumExteriorType.BAKER_BOX
                || this.currentexterior == EnumExteriorType.TYPE_40_TT_CAPSULE
                || this.currentexterior == EnumExteriorType.HARTNELL_EXTERIOR
                || this.currentexterior == EnumExteriorType.HUDOLIN_EXTERIOR) {
            this.portalWidth = 1.275D;
            this.portalHeight = 2.5D;
            this.portalPosX_Z = 0.5D;
            this.portalPosY = 1.269D;
            this.portalPosOldZ = 0.22595D;
        }
        if (this.currentexterior == EnumExteriorType.HUDOLIN_EXTERIOR) {
            this.portalWidth = 1.175D;
            this.portalHeight = 2.3D;
            this.portalPosX_Z = 0.5D;
            this.portalPosY = 1.249D;
            this.portalPosOldZ = 0.2D;
        }
        if (this.currentexterior == EnumExteriorType.HELLBENT_TT_CAPSULE
                || this.currentexterior == EnumExteriorType.FALLOUT_SHELTER_EXTERIOR) {
            this.portalWidth = 0.8D;
            this.portalHeight = 2D;
            this.portalPosOldZ = 0.1725D;
            this.portalPosY = 1.159D;
        }
        if (this.currentexterior == EnumExteriorType.TARDIM_EXTERIOR
                || this.currentexterior == EnumExteriorType.TX3_EXTERIOR) {
            this.portalWidth = 1D;
            this.portalHeight = 2D;
            this.portalPosY = 1D;
            this.portalPosOldZ = -0.95D;
        }
        if (this.currentexterior == EnumExteriorType.SHALKA_EXTERIOR
                || this.currentexterior == EnumExteriorType.BOOTH_EXTERIOR) {
            this.portalWidth = 1.25;
            this.portalPosY = 1.3D;
            this.portalHeight = 2.55D;
            this.portalPosOldZ = 0.225D;
        }
        if (this.currentexterior == EnumExteriorType.NUKA_COLA_EXTERIOR
                || this.currentexterior == EnumExteriorType.ARCADE_CABINET_EXTERIOR
                || this.currentexterior == EnumExteriorType.CLASSIC_EXTERIOR) {
            this.portalWidth = 1.15D;
            this.portalHeight = 2.35D;
            this.portalPosY = 1.249D;
            this.portalPosOldZ = 0.175D;
        }
        if (this.currentexterior == EnumExteriorType.RANI_EXTERIOR) {
            this.portalWidth = 0.9D;
            this.portalHeight = 2.4D;
            this.portalPosOldZ = 0.025D;
            this.portalPosY = 1.35D;
        }
        portal.setOrientationAndSize(new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D),
                this.portalWidth, this.portalHeight);
        if (basicInteriorDoorTile != null) {
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.NORTH) {
                if (basicInteriorDoorTile.getFacing() == Direction.NORTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.865D), new Quaternion(Vector3f.YP, 180.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.SOUTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.135D), new Quaternion(Vector3f.YP, 0.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.EAST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.135D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.5D), new Quaternion(Vector3f.YP, 90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.WEST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.865D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.5D), new Quaternion(Vector3f.YP, -90.0F, true), 1d);
                }
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.SOUTH) {
                if (basicInteriorDoorTile.getFacing() == Direction.NORTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.865D), new Quaternion(Vector3f.YP, 0.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.SOUTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.135D), new Quaternion(Vector3f.YP, 180.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.EAST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.135D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.5D), new Quaternion(Vector3f.YP, -90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.WEST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.865D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.5D), new Quaternion(Vector3f.YP, 90.0F, true), 1d);
                }
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST) {
                if (basicInteriorDoorTile.getFacing() == Direction.NORTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.865D), new Quaternion(Vector3f.YP, -90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.SOUTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.135D), new Quaternion(Vector3f.YP, 90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.EAST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.135D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.5D), new Quaternion(Vector3f.YP, 180.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.WEST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.235D), new Quaternion(Vector3f.YP, 0.0F, true), 1d);
                }
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST) {
                if (basicInteriorDoorTile.getFacing() == Direction.NORTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.865D), new Quaternion(Vector3f.YP, 90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.SOUTH) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.5D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.135D), new Quaternion(Vector3f.YP, -90.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.EAST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.135D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.5D), new Quaternion(Vector3f.YP, 0.0F, true), 1d);
                }
                if (basicInteriorDoorTile.getFacing() == Direction.WEST) {
                    PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION, new Vector3d(
                            this.linked_tardis.interior_door_position.getX() + 0.865D,
                            this.linked_tardis.interior_door_position.getY() + this.portalPosY,
                            this.linked_tardis.interior_door_position.getZ() + 0.5D), new Quaternion(Vector3f.YP, 180.0F, true), 1d);
                }
            }
        }
        if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.NORTH) {
            PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 180, true));
            portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + portalPosX_Z, this.getBlockPos().getY() + portalPosY + upDown, this.getBlockPos().getZ() - portalPosOldZ));
        }
        if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.SOUTH) {
            PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 0, true));
            portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + portalPosX_Z, this.getBlockPos().getY() + portalPosY + upDown, this.getBlockPos().getZ() + 1 + portalPosOldZ));
        }
        if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST) {
            PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, -90, true));
            portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + 1 + portalPosOldZ, this.getBlockPos().getY() + portalPosY + upDown, this.getBlockPos().getZ() + portalPosX_Z));
        }
        if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST) {
            PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 90, true));
            portal.setOriginPos(new Vector3d(this.getBlockPos().getX() - portalPosOldZ, this.getBlockPos().getY() + portalPosY + upDown, this.getBlockPos().getZ() + portalPosX_Z));
        }
        syncToClient();
        if (portal.getDestinationWorld() != null) {
            PortalAPI.spawnServerEntity(portal);
        } else {
            portal.setDestinationDimension(AITDimensions.TARDIS_DIMENSION);
            PortalAPI.spawnServerEntity(portal);
        }
        portal.reloadAndSyncToClient();
        this.portal = portal;
        if (world != null &&
                basicInteriorDoorTile != null &&
                basicInteriorDoorTile != null &&
                basicInteriorDoorTile.portal == null) {
            basicInteriorDoorTile.setPortal(this.portal);
            basicInteriorDoorTile.syncToClient();
            syncToClient();
        }
        this.portal = portal;

        // end TTE
        // start door
        if (this.portal == null) {
            if (this.portalUUID != null) {
                ServerWorld serverworld = AIT.server.getLevel(AITDimensions.TARDIS_DIMENSION);
                this.portal = (Portal) serverworld.getEntity(this.portalUUID);
            }
        } else if (this.getLevel().getEntity(this.portal.getId()) == null) {
            ServerWorld tardisexteriordim = AIT.server.getLevel(linked_tardis.exterior_dimension);
            ServerWorld tardisinteriordim = AIT.server.getLevel(AITDimensions.TARDIS_DIMENSION);
            ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
            ForgeChunkManager.forceChunk(tardisexteriordim, AIT.MOD_ID, this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
            ForgeChunkManager.forceChunk(tardisinteriordim, AIT.MOD_ID, linked_tardis.interior_door_position, chunkPos.x, chunkPos.z, true, true);
            PortalAPI.spawnServerEntity(this.portal);
            this.sync();
        }
        if (this.portal != null) {
            TardisTileEntity tardisTileEntity = (TardisTileEntity) world.getBlockEntity(linked_tardis.exterior_position);
            if (tardisTileEntity != null) {
                if (tardisTileEntity != null) {
                    this.portal.setDestination(new Vector3d(linked_tardis.exterior_position.getX(), linked_tardis.exterior_position.getY(), linked_tardis.exterior_position.getZ()));
                }
            }
        }
        if (this.currentstate == EnumDoorState.CLOSED) {
            if (this.portal != null) {
                this.portal.remove();
                this.portal = null;
                this.portalUUID = null;
            }
        }*/

        // ...

        ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
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
        this.sync();
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
