package io.mdt.ait.nbt.wrapped;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AbsoluteBlockPos {

    private final RegistryKey<World> dimension;
    private final BlockPos position;

    public AbsoluteBlockPos(RegistryKey<World> dimension, BlockPos position) {
        this.dimension = dimension;
        this.position = position;
    }

    public RegistryKey<World> getDimension() {
        return this.dimension;
    }

    public BlockPos get() {
        return this.position;
    }
}
