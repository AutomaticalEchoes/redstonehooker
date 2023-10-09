package com.automaticalechoes.redstonehooker.client.render;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.fakeBlock.FakeBlock;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewer;
import com.automaticalechoes.redstonehooker.client.model.FakeChestModel;
import com.automaticalechoes.redstonehooker.common.block.ContainerProxyBlock;
import com.automaticalechoes.redstonehooker.common.blockentity.ContainerProxyBlockEntity;
import com.automaticalechoes.redstonehooker.common.item.HookerAdjustItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ContainerProxyBlockEntityRender implements BlockEntityRenderer<ContainerProxyBlockEntity> , FakeBlock {
    public static final ResourceLocation BORDER_LOCATION = new ResourceLocation(RedstoneHooker.MODID ,"textures/model/proxy/container/border.png");
    public static final ResourceLocation ACTIVE_LOCATION = new ResourceLocation(RedstoneHooker.MODID ,"textures/model/proxy/container/active.png");
    public static final ResourceLocation NORMAL = new ResourceLocation(RedstoneHooker.MODID,"textures/model/proxy/normal.png");
    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRenderDispatcher;
    private final ModelPart fakeBlock;
    public ContainerProxyBlockEntityRender(BlockEntityRendererProvider.Context p_174416_) {
        this.itemRenderer = p_174416_.getItemRenderer();
        this.blockRenderDispatcher = p_174416_.getBlockRenderDispatcher();
        this.fakeBlock = p_174416_.bakeLayer(FakeChestModel.FAKE_CHEST);
    }

    public void render(ContainerProxyBlockEntity p_112399_, float p_112400_, PoseStack p_112401_, MultiBufferSource p_112402_, int p_112403_, int p_112404_) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockPos pos = p_112399_.getAddress(0);
        Optional<Direction> facing = level.getBlockState(p_112399_.getBlockPos()).getOptionalValue(ContainerProxyBlock.FACING);
        MessagesPreviewer.render(p_112399_, p_112400_, p_112401_, p_112402_, p_112403_, p_112404_);


        boolean flag = RedstoneHooker.ShouldShow();
        boolean active = pos != null && level.getBlockEntity(pos) != null;
        p_112401_.pushPose();
        facing.ifPresent((face) -> {
            p_112401_.translate(0.5,0,0.5);
            p_112401_.mulPose(Axis.YP.rotationDegrees(-face.toYRot()));
            p_112401_.translate(-0.5,0,-0.5);
        });

        if(flag){
            renderFakeBorder(p_112400_,p_112401_,p_112402_,p_112403_,p_112404_, p_112402_.getBuffer(RenderType.entityTranslucentEmissive(BORDER_LOCATION)));
            if(active){
                p_112401_.pushPose();
                p_112401_.scale( 0.5F,0.5F,0.5F);
                p_112401_.translate(0.5F, 0.5F, 0.5F);
                blockRenderDispatcher.renderSingleBlock(level.getBlockState(pos), p_112401_,p_112402_,p_112403_,OverlayTexture.NO_OVERLAY);
//                ItemStack defaultInstance = level.getBlockState(pos).getBlock().asItem().getDefaultInstance();
//                if(defaultInstance.getItem()!= Items.AIR){
//                    this.itemRenderer.renderStatic(defaultInstance, ItemDisplayContext.GROUND, p_112403_, OverlayTexture.NO_OVERLAY,p_112401_, p_112402_,level, 0);
//                }
                p_112401_.popPose();
            }
        }else {
            VertexConsumer buffer = p_112402_.getBuffer(RenderType.entityCutout(active ? ACTIVE_LOCATION : NORMAL));
            renderFakeBorder(p_112400_,p_112401_,p_112402_,p_112403_,p_112404_, buffer);
        }
        p_112401_.popPose();
    }

    @Override
    public ModelPart fakeBlockPart() {
        return fakeBlock;
    }

    @Override
    public float fakeTranslate() {
        return 0.5F;
    }

    @Override
    public float fakeTranslateY() {
        return 1.5F;
    }
}
