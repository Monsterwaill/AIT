package io.mdt.ait.nbt;

import net.minecraft.nbt.CompoundNBT;

public interface NBTSerializeable<T> {
    /**
     * Serializes the class to {@link CompoundNBT}
     *
     * @param t instance of the class that needs to be serialized
     * @param nbt {@link CompoundNBT} where the class will be serialized to
     */
    void serialize(T t, CompoundNBT nbt);

    /**
     * Serializes the class to {@link CompoundNBT}
     * 
     * @implNote This method creates new {@link CompoundNBT}, and if you want to serialize the class into existing {@link CompoundNBT}, use {@link NBTSerializeable#serialize(Object, CompoundNBT)}
     * @param t instance of the class that needs to be serialized
     */
    default CompoundNBT serialize(T t) {
        CompoundNBT nbt = new CompoundNBT();
        this.serialize(t, nbt);

        return nbt;
    }
}
