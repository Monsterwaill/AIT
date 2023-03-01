package io.mdt.ait.tardis.exterior;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.mdt.ait.common.tiles.TARDISTileEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;

public abstract class TARDISExteriorModelSchema<T extends Entity> extends EntityModel<T> {

    private final TARDISExteriorSkinSchema[] skins;

    public TARDISExteriorModelSchema(TARDISExteriorSkinSchema... skins) {
        this.skins = skins;
    }

    public void render(TARDISTileEntity tile, MatrixStack stack, IVertexBuilder buffer, int light, int overlay) {
        for (TARDISExteriorSkinSchema skin : this.skins) {
            skin.pre(tile, stack, buffer, light, overlay, 1, 1, 1, 1);
        }

        this.renderToBuffer(stack, buffer, light, overlay, 1, 1, 1, 1); // FIXME: alpha

        for (TARDISExteriorSkinSchema skin : this.skins) {
            skin.pre(tile, stack, buffer, light, overlay, 1, 1, 1, 1);
        }
    }
}
