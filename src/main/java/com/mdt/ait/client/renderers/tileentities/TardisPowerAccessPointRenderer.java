package com.mdt.ait.client.renderers.tileentities;

import com.mdt.ait.AIT;
import com.mdt.ait.client.models.tileentities.controls.TardisPowerAccessPoint;
import com.mdt.ait.client.renderers.AITRenderTypes;
import com.mdt.ait.common.blocks.TSVBlock;
import com.mdt.ait.common.blocks.TardisPowerAccessPointBlock;
import com.mdt.ait.common.tileentities.TardisPowerAccessPointTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;


public class TardisPowerAccessPointRenderer extends TileEntityRenderer<TardisPowerAccessPointTile> {

    public static final ResourceLocation LOCATION = new ResourceLocation(AIT.MOD_ID, "textures/tileentities/tardis_power_access_point.png");
    public TardisPowerAccessPoint model;
    private final TileEntityRendererDispatcher rendererDispatcher;

    public TardisPowerAccessPointRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.model = new TardisPowerAccessPoint();
        this.rendererDispatcher = rendererDispatcherIn;
    }

    @Override
    public void render(TardisPowerAccessPointTile tile, float PartialTicks, MatrixStack MatrixStackIn, IRenderTypeBuffer Buffer, int CombinedLight, int CombinedOverlay) {
        MatrixStackIn.pushPose();
        MatrixStackIn.translate(0.5, 0, 0.5);
        MatrixStackIn.scale(1f, 1f, 1f);
        MatrixStackIn.translate(0, 1.5f, 0);
        MatrixStackIn.mulPose(Vector3f.XN.rotationDegrees(180.0f));
        MatrixStackIn.mulPose(Vector3f.YP.rotationDegrees(tile.getBlockState().getValue(TardisPowerAccessPointBlock.FACING).toYRot()));
        MatrixStackIn.mulPose(Vector3f.YN.rotationDegrees(180.0f));
        model.render(tile, MatrixStackIn, Buffer.getBuffer(AITRenderTypes.TardisRenderOver(LOCATION)), CombinedLight, CombinedOverlay, 1, 1, 1, 1);
        MatrixStackIn.popPose();
    }
}
