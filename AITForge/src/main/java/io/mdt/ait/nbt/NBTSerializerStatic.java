package io.mdt.ait.nbt;

import net.minecraft.nbt.CompoundNBT;

public interface NBTSerializerStatic<T> extends NBTSerializer<T> {

    @Override
    @Deprecated
    default CompoundNBT serialize(T t) {
        throw new IllegalArgumentException(
                "This method can't be used on this NBT serializer! It probably performs all operations on"
                        + " the root node.");
    }
}
