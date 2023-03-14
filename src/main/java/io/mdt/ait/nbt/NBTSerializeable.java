package io.mdt.ait.nbt;

import net.minecraft.nbt.CompoundNBT;

public interface NBTSerializeable<T> {
    /**
     * Serializes the class to {@link CompoundNBT}
     *
     * @param nbt {@link CompoundNBT} where the class will be serialized to.
     * @param t instance of the class that needs to be serialized.
     */
    void serialize(CompoundNBT nbt, T t);

    /**
     * Serializes the class to {@link CompoundNBT}
     * 
     * @implNote This method creates new {@link CompoundNBT}, and if you want to serialize the class into existing {@link CompoundNBT}, use {@link NBTSerializeable#serialize(CompoundNBT, Object)}
     * @param t instance of the class that needs to be serialized.
     */
    default CompoundNBT serialize(T t) {
        CompoundNBT nbt = new CompoundNBT();
        this.serialize(nbt, t);

        return nbt;
    }
}
