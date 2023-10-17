package com.automaticalechoes.redstonehooker.client.render;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.fakeBlock.FakeBlock;
import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewer;
import com.automaticalechoes.redstonehooker.client.model.EndCauldronLay;
import com.automaticalechoes.redstonehooker.common.block.AddressInner.BlockEntityProxyBlock;
import com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxy.GatewayProxyBlockEntity;
import com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxy.ProxyBlockEntity;
import com.automaticalechoes.redstonehooker.register.BlockRegister;
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

import javax.annotation.Nullable;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class GatewayProxyBlockEntityRender implements BlockEntityRenderer<GatewayProxyBlockEntity>  {
    public static final BlockState FAKE_STATE = Blocks.CAULDRON.defaultBlockState();
    public static final BlockState FAKE_FULL_STATE = Blocks.LAVA_CAULDRON.defaultBlockState();
    public static final BlockState FAKE_NETHER = BlockRegister.FAKE_NETHER_CAULDRON.get().defaultBlockState();
    private final BlockRenderDispatcher blockRenderDispatcher;
    private final ItemRenderer itemRenderer;
    private final ModelPart fakeModel;

    public GatewayProxyBlockEntityRender(BlockEntityRendererProvider.Context p_174416_) {
        this.blockRenderDispatcher = p_174416_.getBlockRenderDispatcher();
        this.itemRenderer = p_174416_.getItemRenderer();
        this.fakeModel = p_174416_.bakeLayer(EndCauldronLay.END_CAULDRON);
    }

    public void render(GatewayProxyBlockEntity p_112399_, float p_112400_, PoseStack p_112401_, MultiBufferSource p_112402_, int p_112403_, int p_112404_) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockPos pos = p_112399_.getAddress(0);
        Optional<Direction> facing = level.getBlockState(p_112399_.getBlockPos()).getOptionalValue(BlockEntityProxyBlock.FACING);

        boolean show = p_112399_.shouldShowMessages();
        boolean active = pos != null && !level.getBlockState(pos).isAir();
        p_112401_.pushPose();
        facing.ifPresent((face) -> {
            p_112401_.translate(0.5,0,0.5);
            p_112401_.mulPose(Axis.YP.rotationDegrees(-face.toYRot()));
            p_112401_.translate(-0.5,0,-0.5);
        });

        if(show){
            MessagesPreviewer.RenderOnFace(p_112399_, p_112400_, p_112401_, p_112402_, p_112403_, p_112404_,Direction.SOUTH);
            if(!active && p_112399_.getAddressItem(0) != null){
                ItemStack addressItem = p_112399_.getAddressItem(0);
                p_112401_.pushPose();
                p_112401_.translate(0.5F,0.3F,0.5F);
                itemRenderer.renderStatic(addressItem,ItemDisplayContext.GROUND,p_112403_,p_112404_,p_112401_,p_112402_,null,11);
                p_112401_.popPose();
            }
        }

        BlockState fake = FAKE_STATE;
        int gatewayType = p_112399_.getGatewayType();
        if(gatewayType == 1 || (!show && gatewayType != 0)){
            fake = FAKE_FULL_STATE;
        }else {
            switch (gatewayType) {
                case 3 -> {
                    p_112401_.pushPose();
                    p_112401_.translate(0.5F, 0.375F, 0.5F);
                    this.fakeModel.render(p_112401_, p_112402_.getBuffer(RenderType.endGateway()), p_112403_, p_112404_);
                    p_112401_.popPose();
                }
                case 2 -> fake = FAKE_NETHER;
            }
        }

        blockRenderDispatcher.renderSingleBlock(fake, p_112401_,p_112402_,p_112403_,OverlayTexture.NO_OVERLAY);

        p_112401_.popPose();
    }


}
