package com.automaticalechoes.redstonehooker.common.netWork;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.common.netWork.packet.AdjustEntity;
import com.automaticalechoes.redstonehooker.common.netWork.packet.BlockEntityDataChange;
import com.automaticalechoes.redstonehooker.common.netWork.packet.ClientBlockEntityCreate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final ResourceLocation CHANNEL_NAME=new ResourceLocation(RedstoneHooker.MODID,"network");
    private static final String PROTOCOL_VERSION = new ResourceLocation(RedstoneHooker.MODID,"1").toString();
    public static SimpleChannel RegisterPacket(){
        final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(()->PROTOCOL_VERSION)
                .simpleChannel();
        INSTANCE.messageBuilder(AdjustEntity.class,1)
                .encoder(AdjustEntity::encode)
                .decoder(AdjustEntity::decode)
                .consumerMainThread(AdjustEntity::onMessage)
                .add();
        INSTANCE.messageBuilder(BlockEntityDataChange.class,2)
                .encoder(BlockEntityDataChange::encode)
                .decoder(BlockEntityDataChange::decode)
                .consumerMainThread(BlockEntityDataChange::onMessage)
                .add();
        INSTANCE.messageBuilder(ClientBlockEntityCreate.class,3)
                .encoder(ClientBlockEntityCreate::encode)
                .decoder(ClientBlockEntityCreate::decode)
                .consumerMainThread(ClientBlockEntityCreate::onMessage)
                .add();
        return INSTANCE;
    }
}
