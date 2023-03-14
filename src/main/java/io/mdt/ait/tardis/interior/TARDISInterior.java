package io.mdt.ait.tardis.interior;

import io.mdt.ait.nbt.NBTSerializeable;
import io.mdt.ait.nbt.NBTUnserializeable;
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

    public static class Serializer implements NBTSerializeable<TARDISInterior>, NBTUnserializeable<TARDISInterior> {

        private static final TARDISInteriorSchema.Serializer SCHEMA_SERIALIZER = new TARDISInteriorSchema.Serializer();

        @Override
        public void serialize(CompoundNBT nbt, TARDISInterior interior) {
            SCHEMA_SERIALIZER.serialize(interior.schema, nbt);
        }

        @Override
        public TARDISInterior unserialize(CompoundNBT nbt) {
            return new TARDISInterior(
                    SCHEMA_SERIALIZER.unserialize(nbt)
            );
        }
    }
}
