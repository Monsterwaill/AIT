package io.mdt.ait.nbt;

import net.minecraft.nbt.CompoundNBT;

public interface NBTDeserializer<T> {
    /**
     * Unserializes a {@link CompoundNBT}
     *
     * @param nbt NBT that needs to be unserialized
     * @return unserialized instance of the class
     */
    T deserialize(CompoundNBT nbt);
}
