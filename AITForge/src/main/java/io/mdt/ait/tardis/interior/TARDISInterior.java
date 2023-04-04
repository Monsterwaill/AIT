package io.mdt.ait.tardis.interior;

import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializer;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.link.impl.TARDISLinkableBasic;
import net.minecraft.nbt.CompoundNBT;

public class TARDISInterior extends TARDISLinkableBasic {

    private TARDISInteriorSchema schema;

    public TARDISInterior(TARDISInteriorSchema schema) {
        this.schema = schema;
    }

    @Override
    public void link(TARDIS tardis) {
        super.link(tardis);
    }

    public TARDISInteriorSchema getSchema() {
        return this.schema;
    }

    public void setSchema(TARDISInteriorSchema schema) {
        this.schema = schema;
    }

    public static class Serializer implements NBTSerializer<TARDISInterior>, NBTDeserializer<TARDISInterior> {

        private static final TARDISInteriorSchema.Serializer SCHEMA_SERIALIZER = new TARDISInteriorSchema.Serializer();

        @Override
        public void serialize(CompoundNBT nbt, TARDISInterior interior) {
            CompoundNBT schema = new CompoundNBT();
            SCHEMA_SERIALIZER.serialize(schema, interior.schema);

            nbt.put("schema", schema);
        }

        @Override
        public TARDISInterior deserialize(CompoundNBT nbt) {
            return new TARDISInterior(SCHEMA_SERIALIZER.deserialize(nbt.getCompound("schema")));
        }
    }
}
