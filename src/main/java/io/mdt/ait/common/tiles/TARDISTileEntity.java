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

        if (this.getLevel() != null
                && !this.getLevel().isClientSide
                && this.getDoor().getPortal() == null) {
            this.spawnPortal();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.worldPosition).inflate(1500, 1500, 1500);
    }

    private static final double[][] values = new double[][] {
            { 0.5D, 0.865D }, { 0.5D, 0.135D }, { 0.865D, 0.5D }, { 0.135D, 0.5D },
    };

    private static final float[][] rvalues = new float[][] {
            { 180.0F, 0.0F, -90.0F, 90.0F },
            { 0.0F, 180.0F, 90.0F, -90.0F },
            { 90.0F, -90.0F, 180.0F, 0.0F },
            { -90.0F, 90.0F, 0.0F, 180.0F },
    };

    public void spawnPortal() {
        Portal3i portal3i = this.getExterior().getSchema().portal();
        BlockPos doorPos = this.getDoor().getDoorPosition();
        ChunkPos chunkPos = new ChunkPos(this.getBlockPos());
        Direction doorFacing = this.getDoor().getTile().getFacing();

        //noinspection DataFlowIssue
        ForgeChunkManager.forceChunk(
                (ServerWorld) this.getLevel(), "ait", this.getBlockPos(), chunkPos.x, chunkPos.z, true, true);
        ForgeChunkManager.forceChunk(TARDISUtil.getTARDISWorld(), "ait", doorPos, chunkPos.x, chunkPos.z, true, true);
        this.sync();

        Portal portal = Portal.entityType.create(this.getLevel());

        portal.setOrientationAndSize(
                new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D), portal3i.width(), portal3i.height());

        int index1 = this.getFacing().ordinal() - 2;
        int index2 = doorFacing.ordinal() - 2;

        PortalManipulation.setPortalTransformation(
                portal, AITDimensions.TARDIS_DIMENSION,
                new Vector3d(
                        doorPos.getX() + values[index2][0],
                        doorPos.getY() + portal3i.y(),
                        doorPos.getZ() + values[index2][1]
                ), new Quaternion(
                        Vector3f.YP, rvalues[index1][index2],
                        true
                ), 1D
        );

        PortalManipulation.rotatePortalBody(
                portal, new Quaternion(Vector3f.YN, rvalues[index1][0], true)
        );

        Vector3d vector3d = new Vector3d(
                this.getBlockPos().getX() + this.getFacing().getNormal().getX() * portal3i.x(),
                this.getBlockPos().getY() + portal3i.y(),
                this.getBlockPos().getZ() + this.getFacing().getNormal().getZ() * portal3i.z()
        );

        if (this.getFacing().getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            vector3d.add(this.getFacing().getNormal().getX(), 0, this.getFacing().getNormal().getZ());
        }

        portal.setOriginPos(vector3d);

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
