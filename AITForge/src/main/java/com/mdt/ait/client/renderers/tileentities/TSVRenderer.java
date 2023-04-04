package com.mdt.ait.client.renderers.tileentities;

import com.mdt.ait.AIT;
import com.mdt.ait.client.models.tileentities.TSV;
import com.mdt.ait.client.renderers.AITRenderTypes;
import com.mdt.ait.common.blocks.TSVBlock;
import com.mdt.ait.common.tileentities.TSVTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class TSVRenderer extends TileEntityRenderer<TSVTile> {

    public static final ResourceLocation LOCATION = new ResourceLocation(AIT.MOD_ID, "textures/tileentities/tsv.png");
    public TSV model;
    private final TileEntityRendererDispatcher rendererDispatcher;

    public TSVRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.model = new TSV();
        this.rendererDispatcher = rendererDispatcherIn;
    }

    @Override
    public void render(
            TSVTile tile,
            float PartialTicks,
            MatrixStack MatrixStackIn,
            IRenderTypeBuffer Buffer,
            int CombinedLight,
            int CombinedOverlay) {
        MatrixStackIn.pushPose();
        MatrixStackIn.translate(0.5, 0, 0.5);
        MatrixStackIn.scale(0.6f, 0.6f, 0.6f);
        MatrixStackIn.translate(0, 1.5f, 0);
        MatrixStackIn.mulPose(Vector3f.XN.rotationDegrees(180.0f));
        MatrixStackIn.mulPose(Vector3f.YP.rotationDegrees(
                tile.getBlockState().getValue(TSVBlock.FACING).toYRot()));
        MatrixStackIn.mulPose(Vector3f.YN.rotationDegrees(180.0f));
        model.render(
                tile,
                MatrixStackIn,
                Buffer.getBuffer(AITRenderTypes.tardisRenderOver(LOCATION)),
                CombinedLight,
                CombinedOverlay,
                1,
                1,
                1,
                1);
        MatrixStackIn.popPose();
    }
}
