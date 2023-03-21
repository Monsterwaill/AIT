package io.mdt.ait.tardis.exterior;

import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializer;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.link.impl.TARDISLinkableBasic;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.nbt.CompoundNBT;

public class TARDISExterior extends TARDISLinkableBasic {

    private TARDISExteriorSchema<?> schema;
    private TARDISTileEntity tile;

    public TARDISExterior(TARDISExteriorSchema<?> schema) {
        this.schema = schema;
    }

    @Override
    public void link(TARDIS tardis) {
        super.link(tardis);

        this.tile = (TARDISTileEntity)
                TARDISUtil.getWorld(tardis.getPosition().getDimension()).getBlockEntity(tardis.getPosition());
        if (this.tile != null) {
            this.tile.link(tardis);
        }
    }

    public TARDISTileEntity getTile() {
        return tile;
    }

    public TARDISExteriorSchema<?> getSchema() {
        return this.schema;
    }

    public void setSchema(TARDISExteriorSchema<?> schema) {
        this.schema = schema;
    }

    public static class Serializer implements NBTSerializer<TARDISExterior>, NBTDeserializer<TARDISExterior> {

        private static final TARDISExteriorSchema.Serializer SCHEMA_SERIALIZER = new TARDISExteriorSchema.Serializer();

        @Override
        public void serialize(CompoundNBT nbt, TARDISExterior exterior) {
            CompoundNBT schema = new CompoundNBT();
            SCHEMA_SERIALIZER.serialize(schema, exterior.schema);

            nbt.put("schema", schema);
        }

        @Override
        public TARDISExterior unserialize(CompoundNBT nbt) {
            return new TARDISExterior(SCHEMA_SERIALIZER.unserialize(nbt.getCompound("schema")));
        }
    }
}
