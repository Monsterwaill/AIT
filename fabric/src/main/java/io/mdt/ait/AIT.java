package io.mdt.ait;

import net.fabricmc.api.ModInitializer;

public class AIT implements ModInitializer {
    @Override
    public void onInitialize() {
        AITCommon.LOGGER.info("Hello Fabric world!");
        AITCommon.init();
    }
}
