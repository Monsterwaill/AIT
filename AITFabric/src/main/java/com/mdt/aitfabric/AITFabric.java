package com.mdt.aitfabric;

import com.mdt.aitfabric.events.ServerStartup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelStorage;

public class AITFabric implements ModInitializer {

    public static MinecraftServer server;
    @Override
    public void onInitialize() {

        System.out.println("Hello World!!!!!!!!!!!");
    }
}
