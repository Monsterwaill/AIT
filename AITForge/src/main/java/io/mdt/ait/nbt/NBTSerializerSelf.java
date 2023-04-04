package io.mdt.ait.nbt;

import net.minecraft.nbt.CompoundNBT;

public interface NBTSerializerSelf<T> extends NBTSerializer<T> {

    @Override
    @Deprecated
    default void serialize(CompoundNBT nbt, T t) {
        throw new IllegalArgumentException(
                "This method can't be used on this NBT serializer! It probably performs all operations on"
                        + " its own node.");
    }
}
