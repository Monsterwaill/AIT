package com.mdt.ait.client.renderers.tardis;

import com.mdt.ait.AIT;
import com.mdt.ait.client.renderers.AITRenderTypes;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.tardis.exterior.TARDISExteriorModelSchema;
import io.mdt.ait.tardis.exterior.impl.model.BasicBoxModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class TARDISRenderer extends TileEntityRenderer<TARDISTileEntity> {

    private ResourceLocation texture;
    public final int maxLight = 15728880;
    public float spinnn = 0;

    // Textures
    public static final ResourceLocation LOCATION =
            new ResourceLocation(AIT.MOD_ID, "textures/exteriors/basic_exterior.png");

    // Lightmaps
    public static final ResourceLocation BASIC_LM_LOCATION =
            new ResourceLocation(AIT.MOD_ID, "textures/exteriors/basic_exterior_emission.png");

    public TARDISExteriorModelSchema model;
    private final TileEntityRendererDispatcher rendererDispatcher;

    public TARDISRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.model = new BasicBoxModel();
        this.rendererDispatcher = dispatcher;
    }

    @Override
    public void render(
            TARDISTileEntity tile, float ticks, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
        if (tile.isLinked()) {
            ++spinnn;

            /*
             * if(materialState != EnumMatState.SOLID) { ++spinny; } else if (materialState ==
             * EnumMatState.SOLID) { spinny = 0; }
             */
            // FIXME: dis

            stack.pushPose();
            // FIXME: it crashes here, TARDIS.getExterior() is null.
            TARDISExteriorModelSchema model = tile.getTARDIS()
                    .getExterior()
                    .getSchema()
                    .render(new RenderInfo(this, stack, buffer, light, overlay, ticks), tile);

            stack.translate(0, 1.5f, 0);
            stack.mulPose(Vector3f.XN.rotationDegrees(180.0f));
            stack.mulPose(Vector3f.YP.rotationDegrees(tile.getBlockState()
                    .getValue(BlockStateProperties.HORIZONTAL_FACING)
                    .toYRot()));
            model.render(
                    tile,
                    stack,
                    buffer.getBuffer(AITRenderTypes.tardisRenderOver(this.texture)),
                    light,
                    overlay,
                    1,
                    1,
                    1,
                    1);
            stack.popPose();

            // model.render(tile, stack,
            // buffer.getBuffer(AITRenderTypes.tardisLightmap(BASIC_LM_LOCATION, false)), maxLight,
            // overlay);
            // model.render(tile, stack,
            // buffer.getBuffer(AITRenderTypes.tardisRenderOver(this.texture)), light, overlay);
        }
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    public TileEntityRendererDispatcher getDispatcher() {
        return this.rendererDispatcher;
    }

    @Override
    public boolean shouldRenderOffScreen(TARDISTileEntity tile) {
        return true;
    }
}
