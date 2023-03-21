package io.mdt.ait.nbt.wrapped;

import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializerStatic;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Contains utility NBT serializers/unserializers that do all the operations on root nbt (read the
 * README file for more information).
 */
public class NBTSerializers {

    public static class Dimension
            implements NBTSerializerStatic<RegistryKey<World>>, NBTDeserializer<RegistryKey<World>> {

        @Override
        public void serialize(CompoundNBT nbt, RegistryKey<World> registryKey) {
            CompoundNBT dimension = new CompoundNBT();
            dimension.putString("name", registryKey.getRegistryName().toString());
            dimension.putString("location", registryKey.location().toString());

            nbt.put("dimension", dimension);
        }

        @Override
        public RegistryKey<World> unserialize(CompoundNBT nbt) {
            CompoundNBT dimension = nbt.getCompound("dimension");

            return RegistryKey.create(
                    RegistryKey.createRegistryKey(new ResourceLocation(dimension.getString("name"))),
                    new ResourceLocation(dimension.getString("location")));
        }
    }

    public static class Position implements NBTSerializerStatic<BlockPos>, NBTDeserializer<BlockPos> {

        @Override
        public void serialize(CompoundNBT nbt, BlockPos pos) {
            this.serialize(nbt, "Pos", pos);
        }

        /**
         * This method is only used internally by {@link AbsolutePosition} to avoid code duplication.
         *
         * @param nbt NBT that this position will serialize to.
         * @param id id that will be used to store the position.
         * @param pos position that will be serialized.
         */
        private void serialize(CompoundNBT nbt, String id, BlockPos pos) {
            nbt.put(id, NBTUtil.writeBlockPos(pos));
        }

        @Override
        public BlockPos unserialize(CompoundNBT nbt) {
            return this.unserialize(nbt, "Pos");
        }

        /**
         * This method is only used internally by {@link AbsolutePosition} to avoid code duplication.
         *
         * @param nbt NBT that this position will unserialize to.
         * @param id id that will be used to get the position.
         */
        private BlockPos unserialize(CompoundNBT nbt, String id) {
            return NBTUtil.readBlockPos(nbt.getCompound(id));
        }
    }

    public static class AbsolutePosition
            implements NBTSerializerStatic<AbsoluteBlockPos>, NBTDeserializer<AbsoluteBlockPos> {

        private static final NBTSerializers.Dimension DIMENSION_SERIALIZER = new NBTSerializers.Dimension();
        private static final NBTSerializers.Position POSITION_SERIALIZER = new NBTSerializers.Position();

        @Override
        public void serialize(CompoundNBT nbt, AbsoluteBlockPos pos) {
            CompoundNBT absolutePos = new CompoundNBT();
            POSITION_SERIALIZER.serialize(absolutePos, "at", pos);
            DIMENSION_SERIALIZER.serialize(absolutePos, pos.getDimension());

            nbt.put("Pos", absolutePos);
        }

        @Override
        public AbsoluteBlockPos unserialize(CompoundNBT nbt) {
            return new AbsoluteBlockPos(
                    DIMENSION_SERIALIZER.unserialize(nbt), POSITION_SERIALIZER.unserialize(nbt, "at"));
        }
    }
}
