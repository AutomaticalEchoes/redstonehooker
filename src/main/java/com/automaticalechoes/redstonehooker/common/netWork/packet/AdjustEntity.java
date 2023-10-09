package com.automaticalechoes.redstonehooker.common.netWork.packet;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record AdjustEntity(int entityID, double vec3x, double vec3y, double vec3z) {

    public static void encode(AdjustEntity msg, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(msg.entityID);
        packetBuffer.writeDouble(msg.vec3x);
        packetBuffer.writeDouble(msg.vec3y);
        packetBuffer.writeDouble(msg.vec3z);
    }

    public static AdjustEntity decode(FriendlyByteBuf packetBuffer) {
        int entityID = packetBuffer.readInt();
        double vec3x = packetBuffer.readDouble();
        double vec3y = packetBuffer.readDouble();
        double vec3z = packetBuffer.readDouble();
        return new AdjustEntity(entityID, vec3x, vec3y, vec3z);
    }

    public static void onMessage(AdjustEntity msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        msg.handleMessage(msg, context.getSender());
        context.setPacketHandled(true);
    }

    public void handleMessage(AdjustEntity msg, ServerPlayer sender) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = level.getEntity(msg.entityID);
        if (entity != null && entity.isAlive()) {
            entity.setPos(msg.vec3x, msg.vec3y, msg.vec3z);
        }

    }
}
