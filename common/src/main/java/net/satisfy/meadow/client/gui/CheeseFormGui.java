package net.satisfy.meadow.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.meadow.Meadow;
import net.satisfy.meadow.client.gui.handler.CheeseFormGuiHandler;

public class CheeseFormGui extends AbstractContainerScreen<CheeseFormGuiHandler> {
    public static final ResourceLocation BACKGROUND;

    public static final int ARROW_X = 78;
    public static final int ARROW_Y = 36;

    public static final int TIME_X = 81;
    public static final int TIME_Y = 8;
    public CheeseFormGui(CheeseFormGuiHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        font = Minecraft.getInstance().font;
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CheeseFormGui.BACKGROUND);
        int posX = this.leftPos;
        int posY = this.topPos;
        guiGraphics.blit(CheeseFormGui.BACKGROUND, posX, posY, 0, 0, this.imageWidth - 1, this.imageHeight);
        this.renderProgressArrow(guiGraphics);
    }

    protected void renderProgressArrow(GuiGraphics guiGraphics) {
        final int progressX = this.menu.getScaledXProgress();
        guiGraphics.blit(BACKGROUND, leftPos + ARROW_X, topPos + ARROW_Y, 176, 4, progressX, 10);
        final int progressY = this.menu.getScaledYProgress();
        guiGraphics.blit(BACKGROUND, leftPos + TIME_X, topPos + TIME_Y, 180, 22, 16, progressY);
    }

    static {
        BACKGROUND = new ResourceLocation(Meadow.MOD_ID, "textures/gui/cheese_form_gui.png");
    }
}
