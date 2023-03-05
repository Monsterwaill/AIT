package io.mdt.ait.tardis.exterior;

import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.nbt.NBTSerializeable;
import io.mdt.ait.nbt.NBTUnserializeable;
import io.mdt.ait.tardis.portal.Portal3i;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Supplier;

public abstract class TARDISExteriorSchema {

    private final String id;
    private final TranslationTextComponent name;

    public TARDISExteriorSchema(String id, String translation) {
        this.id = id;
        this.name = new TranslationTextComponent(translation);
    }

    public abstract <T extends Entity> Supplier<TARDISExteriorModelSchema<T>> model(TARDISTileEntity tile);
    public abstract Portal3i portal();

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name.getString();
    }

    public static class Serializer implements NBTSerializeable<TARDISExteriorSchema>, NBTUnserializeable<TARDISExteriorSchema> {

        @Override
        public void serialize(TARDISExteriorSchema exterior, CompoundNBT nbt) {
            nbt.putString("id", exterior.id);
        }

        @Override
        public TARDISExteriorSchema unserialize(CompoundNBT nbt) {
            return TARDISExteriors.get(nbt.getString("id"));
        }
    }
}
