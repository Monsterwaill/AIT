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
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.link.impl.TARDISLinkableTileEntity;
import io.mdt.ait.tardis.portal.Portal3i;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
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
        super.link(tardis);

        if (this.getLevel() != null && !this.getLevel().isClientSide && !this.getDoor().getPortal().isBuilt()) {
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
        if(this.isLinked()) {
            ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
            ForgeChunkManager.forceChunk((ServerWorld) TARDISUtil.getWorld(this.level.dimension()), "ait", this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
            ForgeChunkManager.forceChunk(TARDISUtil.getTARDISWorld(), "ait", this.getDoor().getDoorPosition(), chunkPos.x, chunkPos.z, true, true);

            this.sync();
            //ServerWorld world = TARDISUtil.getTARDISWorld();
            TARDISDoor door = this.getDoor();
            TARDISInteriorDoorTile doorTile = door.getTile();
            BlockPos doorPos = door.getDoorPosition();
            assert doorPos == door.getTile().getBlockPos();
            System.out.println(doorPos + " " + door.getTile().getBlockPos());

            Portal portal = Portal.entityType.create(this.level);
            portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY(), (this.getBlockPos().getZ() + 1)));
            Portal3i portal3i = this.getExterior().portal();

            portal.setOrientationAndSize(new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D),
                    portal3i.width(), portal3i.height());

            PortalManipulation.setPortalTransformation(portal, AITDimensions.TARDIS_DIMENSION,
                    this.getVectorByDirection(doorTile.getFacing(), portal3i), new Quaternion(
                            Vector3f.YP,
                            this.portalRotation
                                    [this.getFacing().ordinal() - 2]
                                    [doorTile.getFacing().ordinal() - 2],
                            true
                    ), 1d);


            /*if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.NORTH) {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 180, true));
                portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + portal3i.x(), this.getBlockPos().getY() + portal3i.y(), this.getBlockPos().getZ() - portal3i.z()));
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.SOUTH) {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 0, true));
                portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + portal3i.x(), this.getBlockPos().getY() + portal3i.y(), this.getBlockPos().getZ() + 1 + portal3i.z()));
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST) {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, -90, true));
                portal.setOriginPos(new Vector3d(this.getBlockPos().getX() + 1 + portal3i.z(), this.getBlockPos().getY() + portal3i.y(), this.getBlockPos().getZ() + portal3i.x()));
            }
            if (this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST) {
                PortalManipulation.rotatePortalBody(portal, new Quaternion(Vector3f.YN, 90, true));
                portal.setOriginPos(new Vector3d(this.getBlockPos().getX() - portal3i.z(), this.getBlockPos().getY() + portal3i.y(), this.getBlockPos().getZ() + portal3i.x()));
            }*/

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

            this.sync();
            portal.setDestinationDimension(AITDimensions.TARDIS_DIMENSION);
            PortalAPI.spawnServerEntity(portal);
            portal.reloadAndSyncToClient();
            this.getDoor().getPortal().setPortal(portal);
            doorTile.sync();
            this.sync();
        }




        /*Portal3i portal3i = this.getExterior().portal();
        ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
        TARDISInteriorDoorTile doorTile = this.getDoor().getTile();

        if (doorTile != null) {
            ForgeChunkManager.forceChunk((ServerWorld) TARDISUtil.getExteriorLevel(this.getTARDIS()), "ait", this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
            ForgeChunkManager.forceChunk(TARDISUtil.getTARDISWorld(), "ait", this.getDoor().getDoorPosition(), chunkPos.x, chunkPos.z, true, true);
            this.sync();

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

            this.sync();
            this.getDoor().getPortal().build();
        }*/
    }

    public Direction getFacing() {
        return this.getBlockState().getValue(BasicInteriorDoorBlock.FACING);
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
        if(!world.isClientSide) {
            BlockState blockstate = world.getBlockState(pos);
            Block block = blockstate.getBlock();
            //ItemStack key = player.getMainHandItem();
            //if (this.matState == EnumMatState.SOLID) {
                if (!this.getDoor().getState().isLocked()) {
                    if (block instanceof TARDISBlock && hand == Hand.MAIN_HAND) {
                        this.getDoor().getState().next();

                        //if (doorPos != null) {
                        //    this.linked_tardis.setInteriorDoorState(this.currentstate);
                        //}

                        this.getDoor().getTile().sync();

                        switch (this.getDoor().getState().get()) {
                            case FIRST:
                            case BOTH:
                                world.playSound(null, pos, AITSounds.POLICE_BOX_OPEN.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                                break;

                            case CLOSED:
                                world.playSound(null, pos, AITSounds.POLICE_BOX_CLOSE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }

                        this.sync();
                    }
                } else {
                    player.displayClientMessage(new TranslationTextComponent(
                            "Door is locked!").setStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)), true);
                    this.level.playSound(null, pos, AITSounds.TARDIS_LOCK.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.sync();
                }
            //}
        }

        return ActionResultType.SUCCESS;
    }
}
