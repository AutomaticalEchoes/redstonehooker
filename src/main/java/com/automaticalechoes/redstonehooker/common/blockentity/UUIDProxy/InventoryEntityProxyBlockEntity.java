package com.automaticalechoes.redstonehooker.common.blockentity.UUIDProxy;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewable;
import com.automaticalechoes.redstonehooker.common.blockentity.UUIDProxyBlockEntity;
import com.automaticalechoes.redstonehooker.common.item.AddressItem;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class InventoryEntityProxyBlockEntity extends UUIDProxyBlockEntity<InventoryCarrier> implements Container {
    public InventoryEntityProxyBlockEntity(BlockPos p_155630_, BlockState p_155631_) {
        super(BlockEntityRegister.INVENTORY_ENTITY_PROXY_BLOCK_ENTITY.get(), p_155630_, p_155631_);
    }

    @Override
    public int getContainerSize() {
        return proxyTarget != null? proxyTarget.getInventory().getContainerSize() : 0;
    }

    @Override
    public boolean isEmpty() {
        return proxyTarget != null && proxyTarget.getInventory().isEmpty();
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        return proxyTarget.getInventory().getItem(p_18941_);
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return proxyTarget.getInventory().removeItem(p_18942_,p_18943_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return proxyTarget.getInventory().removeItemNoUpdate(p_18951_);
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        proxyTarget.getInventory().setItem(p_18944_,p_18945_);
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return proxyTarget != null && proxyTarget.getInventory().stillValid(p_18946_);
    }

    @Override
    public void clearContent() {

    }

    @Override
    public boolean isProxyNull() {
        return getAddressItem(0).isEmpty() || proxyTarget == null || !(proxyTarget instanceof Entity entity) || !entity.isAlive();
    }

    public void blockChange(){
        if(!(this.level instanceof ServerLevel serverLevel)) return;
        if(this.getAddressItem(0).isEmpty()){
            setError(0);
        }else if(getAddress(0) == null){
            setError(1);
        }else if(serverLevel.getEntity(getAddress(0)) instanceof InventoryCarrier inventoryCarrier){
            setError(0);
            Entity entity = serverLevel.getEntity(getAddress(0));
            this.proxyTarget = inventoryCarrier;
            setProxyTargetID(entity.getId());
        }else{
            setError(4);
        }
    }

}
