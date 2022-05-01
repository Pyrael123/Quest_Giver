package com.feywild.quest_giver.renderer;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.entity.GuildMasterProfession;
import com.feywild.quest_giver.entity.QuestVillager;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.util.ClientEvents;
import com.feywild.quest_giver.util.QuestGiverPlayerData;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;

import java.util.List;
import java.util.Objects;

public class ExclamationMarkerRenderer {

    private static final float ICON_SCALE = 1.0F;
    private static final double MAX_DISTANCE = 64.0; // MAX BLOCK DISTANCE
    private static final double FADE_PERCENTAGE = 50.0; //this cannot be 100.0.
    private static final int Y_POS = -18;
    private static final int X_POS = -8;

    public static void renderExclamationMarker(EntityRenderer<?> renderer, Entity entity, Component component, PoseStack poseStack, MultiBufferSource buffer, int packedLight){

        if((entity instanceof QuestVillager questVillager && questVillager.getVillagerData().getProfession() != VillagerProfession.NONE) ||
                (entity instanceof Villager villager && villager.getVillagerData().getProfession() == GuildMasterProfession.GUILDMASTER.get())){
            //TODO Question mark when the player is on the current Questline - use clientEvents

            boolean exclamationMarkActive = true;

            double squareDistance = renderer.entityRenderDispatcher.distanceToSqr(entity);
            double fadeDistance = ((1.0 - (FADE_PERCENTAGE / 100.0)) * MAX_DISTANCE);
            double opacityDistance =  Mth.clamp(1.0 - ((Math.sqrt(squareDistance) - fadeDistance) / (MAX_DISTANCE - fadeDistance)), 0.0, 1.0);
            float markerHeight = entity.getBbHeight() + 0.5F;

            if (squareDistance > MAX_DISTANCE * MAX_DISTANCE)
            {
                return; //STOP RENDERING MARK IF TO FAR AWAY FROM ENTITY
            }

            poseStack.pushPose();
            poseStack.translate(0.0D, (double)markerHeight, 0.0D);
            poseStack.mulPose(renderer.entityRenderDispatcher.cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.enableDepthTest();

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, (float)opacityDistance );  // 1.0 is full

            if(entity instanceof QuestVillager questVillager) {

                  if (!QuestGiverPlayerData.getQuestList().isEmpty() || QuestGiverPlayerData.getQuestList().contains(questVillager.getQuestNumber().id)) { // if list contains questnumber
                    for (String quest : QuestGiverPlayerData.getQuestList()) {
                        if (Objects.equals(questVillager.getQuestNumber().id, quest)) {
                            exclamationMarkActive = false;
                            break;
                        }
                    }
                 }
            }

            renderMarker( getResourceLocation(exclamationMarkActive), poseStack);

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();

            poseStack.popPose();
        }
    }

    public static ResourceLocation getResourceLocation(boolean active){
                if (active) {
                        return new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/entity/quest_villager/exclamation_mark.png");
                    } else {
                        return new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/entity/quest_villager/question_mark.png");
                    }
                }


    private static void renderMarker(ResourceLocation resource, PoseStack poseStack){
        poseStack.pushPose();
        poseStack.scale(ICON_SCALE, ICON_SCALE, 1.0F);
        renderIcon(resource, poseStack);
        poseStack.popPose();
    }

    private static void renderIcon(ResourceLocation resource, PoseStack poseStack)
    {
        Matrix4f matrix = poseStack.last().pose();

        Minecraft.getInstance().getTextureManager().getTexture(resource).setFilter(false, false);
        RenderSystem.setShaderTexture(0, resource);

        RenderSystem.setShader(GameRenderer:: getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix, X_POS, Y_POS + 16,0).uv( 0,  1).endVertex();
        bufferbuilder.vertex(matrix, X_POS + 16, Y_POS + 16, 0).uv( 1,  1).endVertex();
        bufferbuilder.vertex(matrix, X_POS + 16, Y_POS,	0).uv( 1,  0).endVertex();
        bufferbuilder.vertex(matrix, X_POS, Y_POS, 0).uv( 0,  0).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }
}
