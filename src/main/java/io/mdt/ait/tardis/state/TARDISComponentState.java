package io.mdt.ait.tardis.state;

import io.mdt.ait.nbt.NBTSerializeableSelf;
import io.mdt.ait.nbt.NBTUnserializeable;
import net.minecraft.nbt.CompoundNBT;

public abstract class TARDISComponentState<T extends TARDISComponentState<?>> {

    private final String id;
    private final Serializer<T> serializer;

    public TARDISComponentState(String id, Serializer<T> serializer) {
        this.id = id;
        this.serializer = serializer;
    }

    public String getId() {
        return this.id;
    }

    public Serializer<T> getSerializer() {
        return this.serializer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TARDISComponentState) {
            return this.id.equals(((TARDISComponentState<?>) obj).getId());
        }

        return super.equals(obj);
    }

    public abstract static class Serializer<T extends TARDISComponentState<?>>
            implements NBTSerializeableSelf<Object>, NBTUnserializeable<T> {

        private final Class<T> type;

        /**
         * @param type class of state's parent.
         */
        public Serializer(Class<T> type) {
            this.type = type;
        }

        /**
         * @deprecated DON'T USE THIS METHOD! IT'S FOR INTERNAL USE ONLY!
         */
        @Override
        @Deprecated
        public CompoundNBT serialize(Object obj) {
            if (this.type.isInstance(obj)) {
                @SuppressWarnings("unchecked")
                T t = (T) obj;

                CompoundNBT nbt = new CompoundNBT();
                nbt.putString("id", t.getId());

                this.serializeSelf(nbt, t);
                return nbt;
            }

            throw new IllegalArgumentException("This argument is not state! This should not have happened...");
        }

        public abstract void serializeSelf(CompoundNBT nbt, T t);
    }
}
