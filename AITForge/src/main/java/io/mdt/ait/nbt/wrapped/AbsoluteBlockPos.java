package io.mdt.ait.nbt.wrapped;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

public class AbsoluteBlockPos extends BlockPos {

    private final RegistryKey<World> dimension;

    public AbsoluteBlockPos(RegistryKey<World> dimension, Vector3i pos) {
        this(dimension, pos.getX(), pos.getY(), pos.getZ());
    }

    public AbsoluteBlockPos(RegistryKey<World> dimension, double x, double y, double z) {
        super(x, y, z);

        this.dimension = dimension;
    }

    public RegistryKey<World> getDimension() {
        return this.dimension;
    }
}
