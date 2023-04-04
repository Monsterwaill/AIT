package io.mdt.ait.tardis.interior.impl;

import com.mdt.ait.AIT;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import net.minecraft.util.ResourceLocation;

public class DevInteriorSchema extends TARDISInteriorSchema {

    public DevInteriorSchema() {
        super("dev", "interior.ait.dev", new ResourceLocation(AIT.MOD_ID, "interior/dev"));
    }
}
