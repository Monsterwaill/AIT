package io.mdt.ait.tardis.exterior;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.mdt.ait.common.tiles.TARDISTileEntity;
import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.function.Predicate;

public abstract class TARDISExteriorSkinSchema {

    private final ModelRenderer skinModel;
    private final Predicate<TARDISTileEntity> predicate;

    public TARDISExteriorSkinSchema(Predicate<TARDISTileEntity> predicate) {
        this.skinModel = generateSkinModel();
        this.predicate = predicate;
    }

    public void pre(TARDISTileEntity tile, MatrixStack stack, IVertexBuilder buffer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (this.predicate.test(tile)) {
            this.skinModel.render(stack, buffer, light, overlay, red, green, blue, alpha);
        }
    }

    public void post(TARDISTileEntity tile, MatrixStack stack, IVertexBuilder buffer, int light, int overlay, float red, float green, float blue, float alpha) {

    }

    protected abstract ModelRenderer generateSkinModel();
}
