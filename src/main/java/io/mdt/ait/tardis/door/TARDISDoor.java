package io.mdt.ait.tardis.door;

import com.qouteall.immersive_portals.portal.Portal;
import io.mdt.ait.common.tiles.TARDISInteriorDoorTile;
import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializer;
import io.mdt.ait.nbt.wrapped.NBTSerializers;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.link.impl.TARDISLinkableBasic;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class TARDISDoor extends TARDISLinkableBasic {

    private TARDISInteriorDoorTile tile;
    private final BlockPos position;

    private final TARDISDoorState state;

    private final UUID portalId;
    private Portal portal;

    public TARDISDoor(BlockPos position) {
        this(null, position, new TARDISDoorState());
    }

    private TARDISDoor(UUID portalId, BlockPos position, TARDISDoorState state) {
        this.position = position;
        this.state = state;

        this.portalId = portalId;
    }

    public void link(TARDIS tardis) {
        super.link(tardis);

        this.tile = (TARDISInteriorDoorTile) TARDISUtil.getTARDISWorld().getBlockEntity(position);
        if (this.tile != null) {
            this.tile.link(tardis);
        }

        if (!(this.getTile().getLevel() instanceof ServerWorld))
            return;

        if (this.portalId != null && this.portal == null) {
            this.portal = (Portal) (TARDISUtil.getTARDISWorld()).getEntity(this.portalId);
        }
    }

    public BlockPos getDoorPosition() {
        return this.position;
    }

    public TARDISInteriorDoorTile getTile() {
        return this.tile;
    }

    public TARDISDoorState getState() {
        return this.state;
    }

    public Portal getPortal() {
        return this.portal;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public static class Serializer implements NBTSerializer<TARDISDoor>, NBTDeserializer<TARDISDoor> {

        private static final TARDISDoorState.Serializer STATE_SERIALIZER = new TARDISDoorState.Serializer();
        private static final NBTSerializers.Position POSITION_SERIALIZER = new NBTSerializers.Position();

        @Override
        public void serialize(CompoundNBT nbt, TARDISDoor door) {
            STATE_SERIALIZER.serialize(nbt, door.state);
            POSITION_SERIALIZER.serialize(nbt, door.getDoorPosition());

            nbt.putUUID("portal", door.portal.getUUID());
        }

        @Override
        public TARDISDoor unserialize(CompoundNBT nbt) {
            return new TARDISDoor(
                    nbt.getUUID("portal"),
                    POSITION_SERIALIZER.unserialize(nbt),
                    STATE_SERIALIZER.unserialize(nbt)
            );
        }
    }
}
