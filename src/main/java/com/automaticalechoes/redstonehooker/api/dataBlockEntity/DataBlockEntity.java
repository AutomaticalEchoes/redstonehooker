package com.automaticalechoes.redstonehooker.api.dataBlockEntity;


import com.automaticalechoes.redstonehooker.common.CommonModEvents;
import com.automaticalechoes.redstonehooker.common.netWork.packet.BlockEntityDataChange;
import com.automaticalechoes.redstonehooker.common.netWork.packet.ClientBlockEntityCreate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkDirection;

public abstract class DataBlockEntity extends BlockEntity {
    protected final SynchedBlockEntityData blockEntityData;
    public DataBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
        this.blockEntityData = new SynchedBlockEntityData(this);
        this.define();
    }

    public abstract void define();

    protected void broadCastData(boolean isAll){
        if(this.level instanceof ServerLevel serverLevel){
            Packet<?> packet = CommonModEvents.CHANNEL.toVanillaPacket(new BlockEntityDataChange(this.getBlockPos(), this.blockEntityData, isAll), NetworkDirection.PLAY_TO_CLIENT);
            serverLevel.getServer().getPlayerList().broadcastAll(packet);
        }
    }

    public void onClientCreate(){
        broadCastData(true);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor){
    }

    public SynchedBlockEntityData getBlockEntityData(){
        return this.blockEntityData;
    }

    @Override
    public void setLevel(Level p_155231_) {
        super.setLevel(p_155231_);
        if(p_155231_.isClientSide){
            CommonModEvents.CHANNEL.sendToServer(new ClientBlockEntityCreate(this.getBlockPos()));
        }
    }


}
