package com.automaticalechoes.redstonehooker.client.render;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.fakeBlock.FakeBlock;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewer;
import com.automaticalechoes.redstonehooker.client.model.FakeChestModel;
import com.automaticalechoes.redstonehooker.common.block.AddressInner.InventoryEntityProxyBlock;
import com.automaticalechoes.redstonehooker.common.blockentity.UUIDProxy.InventoryEntityProxyBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class InventoryEntityProxyBlockEntityRender implements BlockEntityRenderer<InventoryEntityProxyBlockEntity> , FakeBlock {
    public static final ResourceLocation BORDER_LOCATION = new ResourceLocation(RedstoneHooker.MODID ,"textures/block/inventory_proxy_border.png");
    public static final ResourceLocation ACTIVE_LOCATION = new ResourceLocation(RedstoneHooker.MODID ,"textures/block/inventory_proxy_active.png");
    public static final ResourceLocation NORMAL = new ResourceLocation(RedstoneHooker.MODID,"textures/block/proxy_normal.png");
    private final EntityRenderDispatcher entityRenderDispatcher;
    private final ItemRenderer itemRenderer;
    private final ModelPart fakeBlock;

    public InventoryEntityProxyBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.entityRenderDispatcher = context.getEntityRenderer();
        this.itemRenderer = context.getItemRenderer();
        this.fakeBlock = context.bakeLayer(FakeChestModel.FAKE_CHEST);
    }

    @Override
    public void render(InventoryEntityProxyBlockEntity p_112563_, float p_112564_, PoseStack p_112565_, MultiBufferSource p_112566_, int p_112567_, int p_112568_) {
        Minecraft instance = Minecraft.getInstance();
        Optional<Direction> facing = instance.level.getBlockState(p_112563_.getBlockPos()).getOptionalValue(InventoryEntityProxyBlock.FACING);
        boolean flag = p_112563_.shouldShowMessages();
        boolean active = p_112563_.getProxyTargetID() != null
                && instance.level.getEntity(p_112563_.getProxyTargetID()) != null
                && instance.level.getEntity(p_112563_.getProxyTargetID()).isAlive();
        p_112565_.pushPose();
        facing.ifPresent((face) ->{
            p_112565_.translate(0.5,0,0.5);
            p_112565_.mulPose(Axis.YP.rotationDegrees(-face.toYRot()));
            p_112565_.translate(-0.5,0,-0.5);
        });
        if(flag){
            renderFakeBorder(p_112564_,p_112565_,p_112566_,p_112567_,p_112568_, RenderType.entityTranslucentEmissive(BORDER_LOCATION));
            if(active){
                Integer address = p_112563_.getProxyTargetID();
                Entity entity = instance.level.getEntity(address);

                p_112565_.pushPose();
                p_112565_.translate(0.5F, 0.25F, 0.5F);
                float f = 0.5F;
                float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
                if ((double)f1 > 0.5D) {
                    f /= f1;
                }
                p_112565_.scale(f, f, f);
                this.entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, p_112564_, p_112565_, p_112566_, p_112567_);
                p_112565_.popPose();
            }else if(!p_112563_.getAddressItem(0).isEmpty()){
                ItemStack addressItem = p_112563_.getAddressItem(0);
                p_112565_.pushPose();
                p_112565_.translate(0.5F, 0.3F, 0.5F);
                this.itemRenderer.renderStatic(addressItem, ItemDisplayContext.GROUND,p_112567_,p_112568_,p_112565_,p_112566_,null,11);
                p_112565_.popPose();
            }
            MessagesPreviewer.RenderOnFace(p_112563_,p_112564_,p_112565_,p_112566_,p_112567_,p_112568_,Direction.SOUTH);
        }else {
            VertexConsumer buffer = p_112566_.getBuffer(RenderType.entityCutout(active ? ACTIVE_LOCATION : NORMAL));
            renderFakeBorder(p_112564_,p_112565_,p_112566_,p_112567_,p_112568_, buffer);
        }
        p_112565_.popPose();

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
