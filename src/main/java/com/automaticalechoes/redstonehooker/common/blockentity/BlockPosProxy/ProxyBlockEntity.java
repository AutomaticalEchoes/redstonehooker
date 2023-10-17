package com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxy;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewable;
import com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxyBlockEntity;
import com.automaticalechoes.redstonehooker.common.item.AddressItem;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ProxyBlockEntity extends BlockPosProxyBlockEntity<BlockEntity> {
    public ProxyBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegister.BLOCK_ENTITY_PROXY_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }


    @Nullable
    public BlockEntity getProxyTarget() {
        return isProxyNull() ? this : proxyTarget;
    }

    public boolean isProxyNull(){
        return this.proxyTarget == null || this.proxyTarget.isRemoved();
    }

    public void blockChange(BlockPos pos){
        this.proxyTargetPos = getAddress(0);
        if(proxyTargetPos == null) {
            setError(1);
            return;
        }

        BlockEntity vanillaBlockEntity = Proxys.getVanillaBlockEntity(this.level, proxyTargetPos);

        if(vanillaBlockEntity == null){
            setError(2);
            return;
        }else if(vanillaBlockEntity instanceof ProxyBlockEntity){
            setError(3);
            return;
        }

        this.proxyTarget = vanillaBlockEntity;
        setError(0);
        if(level instanceof ServerLevel serverLevel){
            serverLevel.getPoiManager().getType(proxyTargetPos)
                    .ifPresent(poiTypeHolder -> serverLevel.getPoiManager().add(pos, poiTypeHolder));
        }
    }
}
