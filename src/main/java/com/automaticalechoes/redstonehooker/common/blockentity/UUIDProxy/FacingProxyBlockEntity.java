package com.automaticalechoes.redstonehooker.common.blockentity.UUIDProxy;

import com.automaticalechoes.redstonehooker.common.blockentity.UUIDProxyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class FacingProxyBlockEntity extends UUIDProxyBlockEntity<Entity> {
    public FacingProxyBlockEntity(BlockEntityType<?> blockEntityType, BlockPos p_155630_, BlockState p_155631_) {
        super(blockEntityType, p_155630_, p_155631_);
    }

    @Override
    public boolean isProxyNull() {
        return getAddressItem(0).isEmpty() || proxyTarget == null || !proxyTarget.isAlive();
    }

    @Override
    public void blockChange() {
        if(!(this.level instanceof ServerLevel serverLevel)) return;
        if(this.getAddressItem(0).isEmpty()){
            setError(0);
        }else if(getAddress(0) == null){
            setError(1);
        }else if(serverLevel.getEntity(getAddress(0)) != null){
            setError(0);
            this.proxyTarget = serverLevel.getEntity(getAddress(0));
            setProxyTargetID(proxyTarget.getId());
        }else{
            setError(4);
        }
    }
}
