package com.automaticalechoes.redstonehooker.common.netWork.packet;


import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.hooker.ILevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientBlockEntityCreate(BlockPos pos) {
    public static void encode(ClientBlockEntityCreate msg, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static ClientBlockEntityCreate decode(FriendlyByteBuf packetBuffer) {
        return new ClientBlockEntityCreate(packetBuffer.readBlockPos());
    }

    public static void onMessage(ClientBlockEntityCreate msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        msg.handleMessage(msg, context.getSender());
        context.setPacketHandled(true);
    }

    public void handleMessage(ClientBlockEntityCreate msg, ServerPlayer sender) {
        if (((ILevel) sender.level()).getVanillaBlockEntity(msg.pos) instanceof DataBlockEntity entity) {
            entity.onClientCreate();
        }
    }
}
