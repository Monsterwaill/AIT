package io.mdt.ait.tardis.exterior;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.mdt.ait.common.tiles.TARDISTileEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;

@SuppressWarnings("rawtypes")
public abstract class TARDISExteriorModelSchema extends EntityModel {

    public TARDISExteriorModelSchema() {}

    @Override
    public void setupAnim(
            Entity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {}

    public void render(
            TARDISTileEntity tile,
            MatrixStack stack,
            IVertexBuilder builder,
            int light,
            int overlay,
            float r,
            float g,
            float b,
            float a) {}

    public void render(TARDISTileEntity tile, MatrixStack stack, IVertexBuilder buffer, int light, int overlay) {
        this.render(tile, stack, buffer, light, overlay, 1, 1, 1, 1);
    }

    /**
     * @deprecated DO NOT USE THIS METHOD. For conveniece, override {@link #render(TARDISTileEntity,
     *     MatrixStack, IVertexBuilder, int, int, float, float, float, float)} method instead!
     */
    @Override
    @Deprecated
    public void renderToBuffer(
            MatrixStack stack, IVertexBuilder builder, int light, int overlay, float r, float g, float b, float a) {
        throw new IllegalArgumentException(
                "Do not use this method. Check the deprecation notice and update your code accordingly.");
    }
}
