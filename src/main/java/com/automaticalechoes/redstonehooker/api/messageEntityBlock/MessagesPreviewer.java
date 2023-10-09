package com.automaticalechoes.redstonehooker.api.messageEntityBlock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class MessagesPreviewer {
    private static final Font font = Minecraft.getInstance().font;

    public static void RenderEachClickFace(MessagesPreviewable p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if(p_112307_.shouldShowMessages()) renderSingleFace(p_112307_,p_112308_,p_112309_,p_112310_,ClickFace(),p_112311_,p_112312_);
    }

    public static void RenderOnClickFace(MessagesPreviewable p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_,Direction direction){
        if(p_112307_.shouldShowMessages() && ClickFace() ==  direction) renderSingleFace(p_112307_,p_112308_,p_112309_,p_112310_,direction,p_112311_,p_112312_);
    }

    public static void RenderOnFace(MessagesPreviewable p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_,Direction direction){
        if(p_112307_.shouldShowMessages()) renderSingleFace(p_112307_,p_112308_,p_112309_,p_112310_,direction,p_112311_,p_112312_);
    }

    public static void renderSingleFace(MessagesPreviewable p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, Direction face, int p_112311_, int p_112312_){
        p_112309_.pushPose();
        p_112309_.translate(0.5D, 0.5D, 0.5D);
        p_112309_.mulPose(face.getRotation());
        p_112309_.mulPose(Axis.XP.rotationDegrees(90));
        p_112309_.scale(0.01F,0.01F,1);
        Component messages = p_112307_.messages();
        p_112309_.translate(0, 0, - (p_112307_.width() / 2 + p_112307_.border()));
        float v = -(p_112307_.width() / 2 - p_112307_.border()) * 100;
        for (int i = 0; i < messages.getSiblings().size(); i++) {
            font.drawInBatch(messages.getSiblings().get(i), v, i * 10 + v, p_112307_.textColor(), false, p_112309_.last().pose(), p_112310_, Font.DisplayMode.NORMAL, 0, p_112311_);
        }
        p_112309_.popPose();
    }

    public static Direction ClickFace(){
        Minecraft instance = Minecraft.getInstance();
        BlockHitResult hitResult = (BlockHitResult) instance.hitResult;
        return hitResult.getDirection();
    }

}
