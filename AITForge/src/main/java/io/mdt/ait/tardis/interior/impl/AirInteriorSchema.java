package io.mdt.ait.tardis.interior.impl;

import com.mdt.ait.AIT;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import net.minecraft.util.ResourceLocation;

public class AirInteriorSchema extends TARDISInteriorSchema {

    public AirInteriorSchema() {
        super("air", "interior.ait.air", new ResourceLocation(AIT.MOD_ID, "interior/air"));
    }
}
