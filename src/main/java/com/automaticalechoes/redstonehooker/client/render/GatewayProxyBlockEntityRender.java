package com.automaticalechoes.redstonehooker.client.render;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.fakeBlock.FakeBlock;
import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewer;
import com.automaticalechoes.redstonehooker.client.model.FakeChestModel;
import com.automaticalechoes.redstonehooker.common.block.BlockEntityProxyBlock;
import com.automaticalechoes.redstonehooker.common.blockentity.GatewayProxyBlockEntity;
import com.automaticalechoes.redstonehooker.common.blockentity.ProxyBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class GatewayProxyBlockEntityRender implements BlockEntityRenderer<GatewayProxyBlockEntity>  {
    public static final ResourceLocation BORDER_LOCATION = new ResourceLocation(RedstoneHooker.MODID ,"textures/block/container_proxy_border.png");
    public static final ResourceLocation ACTIVE_LOCATION = new ResourceLocation(RedstoneHooker.MODID ,"textures/block/container_proxy_active.png");
    public static final ResourceLocation NORMAL = new ResourceLocation(RedstoneHooker.MODID,"textures/block/proxy_normal.png");
    public static final BlockState FAKE_STATE = Blocks.CAULDRON.defaultBlockState();
    private final BlockRenderDispatcher blockRenderDispatcher;
    private final ItemRenderer itemRenderer;
    public GatewayProxyBlockEntityRender(BlockEntityRendererProvider.Context p_174416_) {
        this.blockRenderDispatcher = p_174416_.getBlockRenderDispatcher();
        this.itemRenderer = p_174416_.getItemRenderer();
    }

    public void render(GatewayProxyBlockEntity p_112399_, float p_112400_, PoseStack p_112401_, MultiBufferSource p_112402_, int p_112403_, int p_112404_) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockPos pos = p_112399_.getAddress(0);
        Optional<Direction> facing = level.getBlockState(p_112399_.getBlockPos()).getOptionalValue(BlockEntityProxyBlock.FACING);

        boolean flag = p_112399_.shouldShowMessages();
        boolean active = pos != null && level.getBlockEntity(pos) != null && !(Proxys.getVanillaBlockEntity(level,pos) instanceof ProxyBlockEntity);
        p_112401_.pushPose();
        this.blockRenderDispatcher.renderSingleBlock(FAKE_STATE, p_112401_,p_112402_,p_112403_,OverlayTexture.NO_OVERLAY);
        facing.ifPresent((face) -> {
            p_112401_.translate(0.5,0,0.5);
            p_112401_.mulPose(Axis.YP.rotationDegrees(-face.toYRot()));
            p_112401_.translate(-0.5,0,-0.5);
        });

        if(flag){
            if(active){
                p_112401_.pushPose();
                p_112401_.scale( 0.5F,0.5F,0.5F);
                p_112401_.translate(0.5F, 0.5F, 0.5F);
                blockRenderDispatcher.renderSingleBlock(level.getBlockState(pos), p_112401_,p_112402_,p_112403_,OverlayTexture.NO_OVERLAY);
                p_112401_.popPose();
            }else if(p_112399_.getAddressItem(0) != null){
                ItemStack addressItem = p_112399_.getAddressItem(0);
                p_112401_.pushPose();
                p_112401_.translate(0.5F,0.3F,0.5F);
                itemRenderer.renderStatic(addressItem,ItemDisplayContext.GROUND,p_112403_,p_112404_,p_112401_,p_112402_,null,11);
                p_112401_.popPose();
            }
            MessagesPreviewer.RenderOnFace(p_112399_, p_112400_, p_112401_, p_112402_, p_112403_, p_112404_,Direction.SOUTH);
        }else {
            VertexConsumer buffer = p_112402_.getBuffer(RenderType.entityCutout(active ? ACTIVE_LOCATION : NORMAL));
        }
        p_112401_.popPose();
    }
}
