package com.mdt.ait.core.init.events;

import com.mdt.ait.AIT;
import com.mdt.ait.core.init.AITKeybinds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AIT.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        AITKeybinds.register(event);

        // ScreenManager.register(AITContainerTypes.MONITOR_CONTAINER_TYPE.get(),
        // MonitorScreen::new);
    }
}
