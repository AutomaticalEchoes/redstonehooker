package com.automaticalechoes.redstonehooker.common.netWork.packet;


import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BlockEntityDataChange {
    private final BlockPos pos;
    @Nullable
    private final List<SynchedBlockEntityData.DataItem<?>> packedItems;

    public BlockEntityDataChange(BlockPos pos,SynchedBlockEntityData blockEntityData ,boolean isAll) {
        this.pos = pos;
        if (isAll) {
            this.packedItems = blockEntityData.getAll();
            blockEntityData.clearDirty();
        } else {
            this.packedItems = blockEntityData.packDirty();
        }
    }

    public BlockEntityDataChange(FriendlyByteBuf p_179290_) {
        this.pos = p_179290_.readBlockPos();
        this.packedItems = SynchedBlockEntityData.unpack(p_179290_);
    }

    public static void encode(BlockEntityDataChange msg, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(msg.pos);
        SynchedBlockEntityData.pack(msg.packedItems,packetBuffer);
    }

    public static BlockEntityDataChange decode(FriendlyByteBuf packetBuffer) {
        return new BlockEntityDataChange(packetBuffer);
    }

    public static void onMessage(BlockEntityDataChange msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        msg.handleMessage(msg,context.getSender());
        context.setPacketHandled(true);
    }

    public void handleMessage(BlockEntityDataChange msg, ServerPlayer sender) {
        ClientLevel level = Minecraft.getInstance().level;
        if(level.getBlockEntity(msg.pos) instanceof DataBlockEntity dataBlockEntity){
            dataBlockEntity.getBlockEntityData().assignValues(msg.packedItems);
        }
    }
}
