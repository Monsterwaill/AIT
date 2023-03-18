package io.mdt.ait.nbt;

import net.minecraft.nbt.CompoundNBT;

public interface NBTUnserializeableLate<T> {
    /**
     * Unserializes a {@link CompoundNBT} to the existing object.
     *
     * @param nbt NBT that needs to be unserialized.
     */
    void unserialize(CompoundNBT nbt, T t);
}
