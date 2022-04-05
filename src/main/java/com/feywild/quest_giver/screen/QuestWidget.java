package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.network.quest.SelectQuestSerializer;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import com.feywild.quest_giver.util.QuestGiverTextProcessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class QuestWidget extends Button {


    public static final int WIDTH = 40;
    public static final int HEIGHT = 24;
    private QuestNumber questNumber;
    private BlockPos pos;

    public static final ResourceLocation SELECTION_TEXTURE = new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/gui/looking_glass.png");

    private final SelectableQuest quest;
    private final ItemStack iconStack;

    public QuestWidget(int x, int y, SelectableQuest quest, QuestNumber questNumber, BlockPos pos) {
        super(x, y, WIDTH, HEIGHT, QuestGiverTextProcessor.INSTANCE.processLine(quest.display.title),b -> {});

        this.quest = quest;
        this.iconStack = new ItemStack(quest.icon);
        this.questNumber = questNumber;
        this.pos = pos;
    }

    @Override
    public void onPress() {
        super.onPress();
        QuestGiverMod.getNetwork().channel.sendToServer(new SelectQuestSerializer.Message(this.quest.id, questNumber, pos));
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        RenderHelper.resetColor();
        RenderSystem.setShaderTexture(0, SELECTION_TEXTURE);
        if (this.isHovered(mouseX, mouseY)) {
            this.blit(poseStack, this.x, this.y + 5, 12, 0, 14, 14);
        } else {
            this.blit(poseStack, this.x, this.y + 5, 0, 0, 12, 12);
        }
      //  RenderSystem.setShaderTexture(0, SLOT_TEXTURE);
      //  this.blit(poseStack, this.x + 15, this.y, this.alignment.ordinal() * 25, 0, 24, 24); //nummer van enum * 25
        Minecraft.getInstance().getItemRenderer().renderGuiItem(this.iconStack, this.x + 19, this.y + 4);
        Font font = Minecraft.getInstance().font;
        drawString(poseStack, font, this.getMessage(), this.x + 44, this.y + ((HEIGHT - font.lineHeight) / 2), 0xFFFFFF);
    }

    public boolean isHovered(int x, int y) {
        return this.x <= x && this.x + WIDTH >= x && this.y <= y && this.y + HEIGHT >= y;
    }

}
