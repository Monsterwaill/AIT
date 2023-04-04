package io.mdt.ait;

import io.mdt.ait.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class AITCommon {

    public static final Logger LOGGER = LoggerFactory.getLogger("ait");

    public static void init() {
        LOGGER.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        LOGGER.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        if (Services.PLATFORM.isModLoaded("ait")) {
            LOGGER.info("Hello, AIT!");
        }
    }
}