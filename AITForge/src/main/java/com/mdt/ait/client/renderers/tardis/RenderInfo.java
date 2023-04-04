package com.mdt.ait.client.renderers.tardis;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;

public class RenderInfo {

    public TARDISRenderer renderer;

    public MatrixStack stack;
    public IRenderTypeBuffer buffer;

    public int light;
    public int overlay;

    public float ticks;

    public RenderInfo(
            TARDISRenderer renderer, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float ticks) {
        this.renderer = renderer;

        this.stack = stack;
        this.buffer = buffer;

        this.light = light;
        this.overlay = overlay;

        this.ticks = ticks;
    }
}
