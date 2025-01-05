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
import net.satisfy.meadow.client.gui.handler.CookingCauldronGuiHandler;

public class CookingCauldronGui extends AbstractContainerScreen<CookingCauldronGuiHandler> {
    public static final ResourceLocation BACKGROUND;

    public static final int ARROW_X = 92;
    public static final int ARROW_Y = 10;

    public CookingCauldronGui(CookingCauldronGuiHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        font = Minecraft.getInstance().font;
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    protected void renderProgressArrow(GuiGraphics guiGraphics) {
        int progress = this.menu.getScaledProgress(17);
        guiGraphics.blit(BACKGROUND, leftPos + ARROW_X, topPos + ARROW_Y, 178, 16, progress, 29);
    }

    protected void renderBurnIcon(GuiGraphics guiGraphics, int posX, int posY) {
        if (menu.isBeingBurned()) {
            guiGraphics.blit(BACKGROUND, posX + 124, posY + 51, 176, 0, 17, 15);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CookingCauldronGui.BACKGROUND);
        int posX = this.leftPos;
        int posY = this.topPos;
        guiGraphics.blit(CookingCauldronGui.BACKGROUND, posX, posY, 0, 0, this.imageWidth - 1, this.imageHeight);
        this.renderProgressArrow(guiGraphics);
        this.renderBurnIcon(guiGraphics, posX, posY);
    }
    static {
        BACKGROUND = new ResourceLocation(Meadow.MOD_ID, "textures/gui/cooking_cauldron_gui.png");
    }
}