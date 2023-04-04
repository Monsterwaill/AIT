package com.mdt.ait.mixin;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldRenderer.class)
public class SkyBoxMixin {

    @Shadow
    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
}
