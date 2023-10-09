package com.automaticalechoes.redstonehooker.client;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.fakeBlock.FakeBlock;
import com.automaticalechoes.redstonehooker.client.model.FakeChestModel;
import com.automaticalechoes.redstonehooker.client.render.InventoryEntityProxyBlockEntityRender;
import com.automaticalechoes.redstonehooker.client.render.ContainerProxyBlockEntityRender;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT,modid = RedstoneHooker.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FakeBlock.FAKE_BLOCK,FakeBlock::createBlockLayer);
        event.registerLayerDefinition(FakeChestModel.FAKE_CHEST,FakeChestModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void RegisterRenders(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegister.CONTAINER_PROXY_BLOCK_ENTITY.get(), ContainerProxyBlockEntityRender::new);
        event.registerBlockEntityRenderer(BlockEntityRegister.INVENTORY_ENTITY_PROXY_BLOCK_ENTITY.get(), InventoryEntityProxyBlockEntityRender::new);
    }
}
