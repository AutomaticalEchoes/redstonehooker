package com.automaticalechoes.redstonehooker.api.fakeBlock;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public interface FakeBlock {
    ModelLayerLocation FAKE_BLOCK = new ModelLayerLocation(new ResourceLocation(RedstoneHooker.MODID, "fake_block_model"), "main");

    ModelPart fakeBlockPart();

    static LayerDefinition createBlockLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("wind", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    default void renderFakeBorder(float p_112400_, PoseStack p_112401_, MultiBufferSource p_112402_, int p_112403_, int p_112404_, Material material){
        VertexConsumer vertexconsumer1 = material.buffer(p_112402_, RenderType::entityTranslucent);
        renderFakeBorder(p_112400_, p_112401_, p_112402_, p_112403_, p_112404_, vertexconsumer1);
    }

    default void renderFakeBorder(float p_112400_, PoseStack p_112401_, MultiBufferSource p_112402_, int p_112403_, int p_112404_, RenderType renderType){
        VertexConsumer vertexconsumer1 = p_112402_.getBuffer(renderType);
        renderFakeBorder(p_112400_, p_112401_, p_112402_, p_112403_, p_112404_, vertexconsumer1);
    }

    default void renderFakeBorder(float p_112400_, PoseStack p_112401_, MultiBufferSource p_112402_, int p_112403_, int p_112404_, VertexConsumer vertexconsumer1){
        p_112401_.pushPose();
        p_112401_.scale(fakeScaleX(),fakeScaleY(),fakeScaleZ());
        p_112401_.translate(fakeTranslateX(),fakeTranslateY(),fakeTranslateZ());
        this.fakeBlockPart().render(p_112401_, vertexconsumer1, p_112403_, p_112404_);
        p_112401_.popPose();
    }

    default float fakeScale(){
        return 1.0F;
    }

    default float fakeScaleX(){
        return fakeScale();
    }

    default float fakeScaleY(){
        return fakeScale();
    }

    default float fakeScaleZ(){
        return fakeScale();
    }

    default float fakeTranslate(){
        return 0F;
    }

    default float fakeTranslateX(){
        return fakeTranslate();
    }

    default float fakeTranslateY(){
        return fakeTranslate();
    }

    default float fakeTranslateZ(){
        return fakeTranslate();
    }
}
