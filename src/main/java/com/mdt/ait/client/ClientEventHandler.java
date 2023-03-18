package com.mdt.ait.client;

import com.mdt.ait.AIT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AIT.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    /*@SubscribeEvent
    public static void replaceSkybox(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        ClientWorld level = mc.level;
        DimensionRenderInfo effects = level.effects();
        RegistryKey<World> dim = level.dimension();

        if (effects.getSkyRenderHandler() == null) {
            if (dim.equals(AITDimensions.TARDIS_DIMENSION)) effects.setSkyRenderHandler(TardisSkyRenderer.INSTANCE);
        } else {

        }
    }*/

}
