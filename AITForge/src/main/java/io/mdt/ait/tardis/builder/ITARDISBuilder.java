package io.mdt.ait.tardis.builder;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITARDISBuilder {
    ITARDISBuilder at(BlockPos position, RegistryKey<World> dimension);

    ITARDISBuilder exterior(TARDISExteriorSchema exterior);

    ITARDISBuilder interior(TARDISInteriorSchema interior);

    TARDIS build();
}
