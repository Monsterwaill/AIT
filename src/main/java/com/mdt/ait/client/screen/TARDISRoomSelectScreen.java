package com.mdt.ait.client.screen;

import com.mdt.ait.AIT;
import com.mdt.ait.common.items.SonicItem;
import com.mdt.ait.core.init.AITDimensions;
import com.mdt.ait.tardis.structures.TARDISRoomGenerator;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TARDISRoomSelectScreen extends Screen {
    private static final ResourceLocation GUI =
            new ResourceLocation(AIT.MOD_ID, "textures/screens/structure_select_screen.png");
    private final SonicItem parentSonic;
    private final int imageHeight;
    private final int imageWidth;
    private String current_selection;
    private int current_selection_id;
    private final FontRenderer fontRenderer = Minecraft.getInstance().font;
    private TARDISRoomGenerator generator;

    public TARDISRoomSelectScreen(ITextComponent label, SonicItem parentSonic) {
        super(label);
        this.parentSonic = parentSonic;
        this.imageWidth = 176;
        this.imageHeight = 106;
        this.generator = new TARDISRoomGenerator(AIT.server.getLevel(AITDimensions.TARDIS_DIMENSION), null);
    }

    // @TODO FIX LOCATIONS OF BUTTONS
    @Override
    protected void init() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        current_selection_id = 0;
        current_selection = this.generator.structureNameList.get(current_selection_id);
        this.addButton(new Button(
                (this.width / 2) + 96,
                this.height / 2,
                20,
                20,
                new StringTextComponent(">"),
                (button) -> onPressRightButton()));
        this.addButton(new Button(
                (this.width / 2) - 96,
                this.height / 2,
                20,
                20,
                new StringTextComponent("<"),
                (button) -> onPressLeftButton()));
        this.addButton(new Button(
                (this.width / 2),
                this.height / 2 - this.imageHeight / 4,
                20,
                20,
                new StringTextComponent("SELECT"),
                (button) -> onPressSelectButton()));
    }

    private void onPressSelectButton() {
        this.parentSonic.structure_name = current_selection;
    }

    private void onPressLeftButton() {
        current_selection_id--;
        if (current_selection_id == -1) {
            current_selection_id = this.generator.structureNameList.size() - 1;
        }
        current_selection = this.generator.structureNameList.get(current_selection_id);
    }

    private void onPressRightButton() {
        current_selection_id++;
        if (current_selection_id == this.generator.structureNameList.size()) {
            current_selection_id = 0;
        }
        current_selection = this.generator.structureNameList.get(current_selection_id);
    }

    @Override
    public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(
                matrixStack,
                fontRenderer,
                this.generator.toStructureName(current_selection),
                this.width / 2,
                this.height / 2,
                0xA7C7E7);
        super.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public void renderBackground(MatrixStack pMatrixStack) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        pMatrixStack.pushPose();
        pMatrixStack.scale(2f, 2f, 2f);
        this.blit(
                pMatrixStack,
                (this.width) - this.imageWidth / 2,
                (this.height) - this.imageHeight,
                0,
                0,
                this.imageWidth,
                this.imageHeight);
        pMatrixStack.popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}