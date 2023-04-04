package io.mdt.ait.tardis.exterior.impl;

import com.mdt.ait.AIT;
import com.mdt.ait.client.renderers.AITRenderTypes;
import com.mdt.ait.client.renderers.tardis.RenderInfo;
import com.mdt.ait.core.init.AITDimensions;
import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.tardis.door.TARDISDoorState;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.exterior.impl.model.BasicBoxModel;
import io.mdt.ait.tardis.portal.Portal3i;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BasicBoxExteriorSchema extends TARDISExteriorSchema<BasicBoxModel> {

    private static final ResourceLocation LOCATION =
            new ResourceLocation(AIT.MOD_ID, "textures/exteriors/basic_exterior.png");
    private static final ResourceLocation BASIC_LM_LOCATION =
            new ResourceLocation(AIT.MOD_ID, "textures/exteriors/basic_exterior_emission.png");

    private static final ResourceLocation CHRISTMAS_LOCATION =
            new ResourceLocation(AIT.MOD_ID, "textures/exteriors/holidays/basic_exterior_christmas.png");
    private static final ResourceLocation SNOW_LOCATION =
            new ResourceLocation(AIT.MOD_ID, "textures/exteriors/biomes/snow/basic_exterior_snow.png");
    private static final ResourceLocation SAND_LOCATION =
            new ResourceLocation(AIT.MOD_ID, "textures/exteriors/biomes/sand/basic_exterior_sand.png");

    public BasicBoxExteriorSchema() {
        super("basic", "exterior.ait.basic");
    }

    @Override
    public BasicBoxModel render(RenderInfo info, TARDISTileEntity tile, BasicBoxModel model) {
        this.smithMintPosterText(info);
        LocalDate localdate = LocalDate.now();
        int i = localdate.get(ChronoField.DAY_OF_MONTH);
        int j = localdate.get(ChronoField.MONTH_OF_YEAR);

        if (j == 12 && (i == 24 || i == 25)) {
            info.renderer.setTexture(CHRISTMAS_LOCATION);
            model.christmas_stuff.visible = true;
        } else if (tile.getLevel().getBiome(tile.getBlockPos()).getPrecipitation() == Biome.RainType.SNOW) {
            info.renderer.setTexture(SNOW_LOCATION);
            model.christmas_stuff.visible = false;
        } else if (tile.getLevel().getBiome(tile.getBlockPos()).getPrecipitation() == Biome.RainType.NONE) {
            if (tile.getLevel().dimension() != World.NETHER
                    || tile.getLevel().dimension() != AITDimensions.GALLIFREY
                    || tile.getLevel().dimension() != World.END
                    || tile.getLevel().dimension() != AITDimensions.TARDIS_DIMENSION
                    || tile.getLevel().dimension() != AITDimensions.VORTEX_DIMENSION) {
                info.renderer.setTexture(SAND_LOCATION);
                model.christmas_stuff.visible = false;
            }
        } else {
            info.renderer.setTexture(LOCATION);
            model.christmas_stuff.visible = false;
        }

        boolean doorsVisible = tile.getDoor().getState() == TARDISDoorState.CLOSED;
        model.right_door.visible = doorsVisible;
        model.left_door.visible = doorsVisible;

        info.stack.translate(0.5f, 0f, 0.5f);
        info.stack.scale(0.725f, 0.725f, 0.725f);
        info.stack.pushPose();
        info.stack.translate(0, 1.4949, 0);
        info.stack.scale(1.001f, 1.0001f, 1.001f);
        info.stack.mulPose(Vector3f.XN.rotationDegrees(180.0f));
        info.stack.mulPose(Vector3f.YP.rotationDegrees(tile.getBlockState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING)
                .toYRot()));
        model.render(
                tile,
                info.stack,
                info.buffer.getBuffer(AITRenderTypes.tardisLightmap(BASIC_LM_LOCATION, false)),
                info.light,
                info.overlay,
                1,
                1,
                1,
                1);
        info.stack.popPose();

        return model;
    }

    private void smithMintPosterText(RenderInfo info) {
        info.stack.pushPose();
        info.stack.translate(0.925f, 2.75f, -0.37f);
        info.stack.scale(0.0125f, 0.0125f, 0.0125f);
        info.stack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        info.stack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        FontRenderer fontRenderer = info.renderer.getDispatcher().getFont();
        IReorderingProcessor irp = new StringTextComponent("POLICE -=- BOX").getVisualOrderText();
        fontRenderer.drawInBatch(
                irp, -5, 5, 16777215, false, info.stack.last().pose(), info.buffer, false, 0, info.light);
        info.stack.popPose();
        info.stack.pushPose();
        info.stack.translate(1.37f, 2.75f, 0.925f);
        info.stack.scale(0.0125f, 0.0125f, 0.0125f);
        info.stack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        info.stack.mulPose(Vector3f.YP.rotationDegrees(-90.0f));
        fontRenderer.drawInBatch(
                irp, -5, 5, 16777215, false, info.stack.last().pose(), info.buffer, false, 0, info.light);
        info.stack.popPose();
        info.stack.pushPose();
        info.stack.translate(-0.37f, 2.75f, 0.09f);
        info.stack.scale(0.0125f, 0.0125f, 0.0125f);
        info.stack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        info.stack.mulPose(Vector3f.YP.rotationDegrees(90.0f));
        fontRenderer.drawInBatch(
                irp, -5, 5, 16777215, false, info.stack.last().pose(), info.buffer, false, 0, info.light);
        info.stack.popPose();
        info.stack.pushPose();
        info.stack.translate(0.09f, 2.75f, 1.37f);
        info.stack.scale(0.0125f, 0.0125f, 0.0125f);
        info.stack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        fontRenderer.drawInBatch(
                irp, -5, 5, 16777215, false, info.stack.last().pose(), info.buffer, false, 0, info.light);
        info.stack.popPose();
    }

    @Override
    protected BasicBoxModel model() {
        return new BasicBoxModel();
    }

    @Override
    public Portal3i portal() {
        return new Portal3i(1.275D, 2.5D, 0.5D, 1.269D, 0.22595D);
    }
}
