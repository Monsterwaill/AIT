package com.mdt.ait.client.renderers.tileentities;

import com.mdt.ait.AIT;
import com.mdt.ait.client.models.tileentities.controls.ExteriorFacingControl;
import com.mdt.ait.client.renderers.AITRenderTypes;
import com.mdt.ait.common.blocks.ExteriorFacingControlBlock;
import com.mdt.ait.common.tileentities.ExteriorFacingControlTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ExteriorFacingControlRenderer extends TileEntityRenderer<ExteriorFacingControlTile> {

    public static final ResourceLocation LOCATION =
            new ResourceLocation(AIT.MOD_ID, "textures/tileentities/exterior_facing_control.png");
    public static final ResourceLocation LIGHTING =
            new ResourceLocation(AIT.MOD_ID, "textures/tileentities/exterior_facing_control_emission.png");
    public ExteriorFacingControl model;
    public float rotationFacing;

    public ExteriorFacingControlRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.model = new ExteriorFacingControl();
        this.rotationFacing = 0;
    }

    @Override
    public void render(
            ExteriorFacingControlTile tile,
            float PartialTicks,
            MatrixStack MatrixStackIn,
            IRenderTypeBuffer Buffer,
            int CombinedLight,
            int CombinedOverlay) {
        // TODO: math this
        if (tile.getDirection() == Direction.NORTH) {
            this.rotationFacing = 0;
        }
        if (tile.getDirection() == Direction.EAST) {
            this.rotationFacing = 90;
        }
        if (tile.getDirection() == Direction.SOUTH) {
            this.rotationFacing = 180;
        }
        if (tile.getDirection() == Direction.WEST) {
            this.rotationFacing = -90;
        }

        MatrixStackIn.pushPose();
        MatrixStackIn.translate(0.5, 0, 0.5);
        MatrixStackIn.scale(1f, 1f, 1f);
        MatrixStackIn.translate(0, 1.5f, 0);
        MatrixStackIn.mulPose(Vector3f.XN.rotationDegrees(180.0f));
        MatrixStackIn.mulPose(Vector3f.YP.rotationDegrees(
                tile.getBlockState().getValue(ExteriorFacingControlBlock.FACING).toYRot()));
        MatrixStackIn.pushPose();
        MatrixStackIn.mulPose(Vector3f.YP.rotationDegrees(this.rotationFacing));
        MatrixStackIn.scale(0.125f, 0.125f, 0.125f);
        MatrixStackIn.translate(0, 4.5, 0);
        this.model.box.render(
                MatrixStackIn,
                Buffer.getBuffer(AITRenderTypes.tardisLightmap(LIGHTING, false)),
                CombinedLight,
                CombinedOverlay,
                1,
                1,
                1,
                1);
        MatrixStackIn.popPose();
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
        model.render(
                tile,
                MatrixStackIn,
                Buffer.getBuffer(AITRenderTypes.tardisLightmap(LIGHTING, false)),
                CombinedLight,
                CombinedOverlay,
                1,
                1,
                1,
                1);
        MatrixStackIn.popPose();
    }
}
