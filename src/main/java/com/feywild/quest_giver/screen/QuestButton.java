package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class QuestButton extends Button {

    public static final int WIDTH = 90;
    public static final int HEIGHT = 25;

    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/gui/quest_button.png");

    public QuestButton(int x, int y, Component message, OnPress onPress) {
        super(x, y, WIDTH, HEIGHT, message, onPress);
    }

    @Override
    public void renderButton(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BUTTON_TEXTURE);
        RenderSystem.setShaderColor(0.0F,0.0F,0.0F, 0.5F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(poseStack, this.x, this.y, 0, 0, WIDTH, HEIGHT);
        this.renderBg(poseStack, minecraft, mouseX, mouseY);
        int j = getFGColor();
        drawCenteredString(poseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
