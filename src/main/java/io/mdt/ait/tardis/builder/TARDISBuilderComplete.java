package io.mdt.ait.tardis.builder;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TARDISBuilderComplete implements ITARDISBuilder {

    private final TARDISBuilder builder;

    public TARDISBuilderComplete(TARDISBuilder builder) {
        this.builder = builder;
    }

    @Override
    public ITARDISBuilder at(BlockPos position, RegistryKey<World> dimension) {
        this.builder.at(position, dimension);
        return this;
    }

    @Override
    public ITARDISBuilder exterior(TARDISExteriorSchema exterior) {
        this.builder.exterior(exterior);
        return this;
    }

    @Override
    public ITARDISBuilder interior(TARDISInteriorSchema interior) {
        this.builder.interior(interior);
        return this;
    }

    @Override
    public TARDIS build() {
        return new TARDIS(
                this.builder.uuid, this.builder.position, this.builder.dimension,
                this.builder.exterior, this.builder.interior
        );
    }
}
