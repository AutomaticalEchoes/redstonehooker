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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class ArmorProxyBlockEntity extends UUIDProxyBlockEntity<LivingEntity> implements Container {
    public ArmorProxyBlockEntity(BlockPos p_155630_, BlockState p_155631_) {
        super(BlockEntityRegister.ARMOR_PROXY_BLOCK_ENTITY.get(), p_155630_, p_155631_);
    }

    @Override
    public int getContainerSize() {
        return proxyTarget != null? 4 : 0;
    }

    @Override
    public boolean isEmpty() {
        if(proxyTarget == null) return false;
        for (ItemStack armorSlot : proxyTarget.getArmorSlots()) {
            if(!armorSlot.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        if(p_18941_ < 0 || p_18941_ > 4) return ItemStack.EMPTY;
        return proxyTarget.getItemBySlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR,p_18941_));
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        if(p_18942_ < 0 || p_18942_ > 4 || p_18943_ ==0 ) return ItemStack.EMPTY;
        return proxyTarget.getItemBySlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR,p_18942_)).split(1);
    }

    @Override
    public boolean canPlaceItem(int p_18952_, ItemStack p_18953_) {
        if(p_18952_ < 0 || p_18952_ > 4) return false;
        EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(p_18953_);
        return equipmentSlot.getType() == EquipmentSlot.Type.ARMOR && equipmentSlot.getIndex() == p_18952_ && proxyTarget.getItemBySlot(equipmentSlot).isEmpty();
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return removeItem(p_18951_,1);
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        proxyTarget.setItemSlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR,p_18944_),p_18945_);
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return proxyTarget != null;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public boolean isProxyNull() {
        return getAddressItem(0).isEmpty() || proxyTarget == null || !proxyTarget.isAlive();
    }

    public void blockChange(){
        if(!(this.level instanceof ServerLevel serverLevel)) return;
        if(this.getAddressItem(0).isEmpty()){
            setError(0);
        }else if(getAddress(0) == null){
            setError(1);
        }else if(serverLevel.getEntity(getAddress(0)) instanceof LivingEntity livingEntity && livingEntity.isAlive()){
            setError(0);
            this.proxyTarget = livingEntity;
            setProxyTargetID(livingEntity.getId());
        }else{
            setError(4);
        }
    }

}
