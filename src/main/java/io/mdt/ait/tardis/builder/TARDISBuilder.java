package io.mdt.ait.tardis.builder;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.exterior.TARDISExteriors;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import io.mdt.ait.tardis.interior.TARDISInteriors;
import java.util.UUID;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Used to create a TARDIS with builder pattern */
public class TARDISBuilder implements ITARDISBuilder {

    protected final UUID uuid;

    protected BlockPos position;
    protected RegistryKey<World> dimension;

    protected TARDISExteriorSchema exterior = TARDISExteriors.get("basic");
    protected TARDISInteriorSchema interior = TARDISInteriors.get("dev");

    public TARDISBuilder(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public ITARDISBuilder at(BlockPos position, RegistryKey<World> dimension) {
        this.position = position;
        this.dimension = dimension;

        return new TARDISBuilderComplete(
                this); // after the position and UUID are set, TARDIS can be created with default exterior
        // and interior schemas
    }

    @Override
    public ITARDISBuilder exterior(TARDISExteriorSchema exterior) {
        this.exterior = exterior;
        return this;
    }

    @Override
    public ITARDISBuilder interior(TARDISInteriorSchema interior) {
        this.interior = interior;
        return this;
    }

    @Override
    public TARDIS build() {
        throw new IllegalArgumentException("TARDIS' position is not set!");
    }
}
