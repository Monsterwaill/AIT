package io.mdt.ait.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class NBTSerializers {

    public static class Dimension implements NBTSerializeable<RegistryKey<World>>, NBTUnserializeable<RegistryKey<World>> {

        @Override
        public void serialize(RegistryKey<World> registryKey, CompoundNBT nbt) {
            nbt.putString("name", registryKey.getRegistryName().toString());
            nbt.putString("location", registryKey.location().toString());
        }

        @Override
        public RegistryKey<World> unserialize(CompoundNBT nbt) {
            return RegistryKey.create(
                    RegistryKey.createRegistryKey(
                            new ResourceLocation(nbt.getString("name"))
                    ),
                    new ResourceLocation(nbt.getString("location"))
            );
        }
    }
}
