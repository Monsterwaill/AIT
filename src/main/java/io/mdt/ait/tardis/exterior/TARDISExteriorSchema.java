package io.mdt.ait.tardis.exterior;

import com.mdt.ait.client.renderers.tardis.RenderInfo;
import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializer;
import io.mdt.ait.tardis.portal.Portal3i;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class TARDISExteriorSchema<T extends TARDISExteriorModelSchema> {

    private final String id;
    private final TranslationTextComponent name;

    public TARDISExteriorSchema(String id, String translation) {
        this.id = id;
        this.name = new TranslationTextComponent(translation);
    }

    public abstract T render(RenderInfo info, TARDISTileEntity tile, T model);

    protected abstract T model();

    public abstract Portal3i portal();

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name.getString();
    }

    public T render(RenderInfo info, TARDISTileEntity tile) {
        return this.render(info, tile, this.model());
    }

    public static class Serializer
            implements NBTSerializer<TARDISExteriorSchema<?>>, NBTDeserializer<TARDISExteriorSchema<?>> {

        @Override
        public void serialize(CompoundNBT nbt, TARDISExteriorSchema<?> exterior) {
            nbt.putString("id", exterior.id);
        }

        @Override
        public TARDISExteriorSchema<?> unserialize(CompoundNBT nbt) {
            return TARDISExteriors.get(nbt.getString("id"));
        }
    }
}
