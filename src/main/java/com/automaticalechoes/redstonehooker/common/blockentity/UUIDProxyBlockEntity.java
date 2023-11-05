package com.automaticalechoes.redstonehooker.common.blockentity;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewable;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class UUIDProxyBlockEntity<T> extends DataBlockEntity implements MessagesPreviewable, AddressItemInner<UUID> {
    private static final EntityDataAccessor<ItemStack> ADDRESS_ITEM = SynchedBlockEntityData.defineId(UUIDProxyBlockEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> ENTITY_ID = SynchedBlockEntityData.defineId(UUIDProxyBlockEntity.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ERROR_TYPE =  SynchedBlockEntityData.defineId(UUIDProxyBlockEntity.class,EntityDataSerializers.INT);
    @Nullable
    protected T proxyTarget = null;
    public UUIDProxyBlockEntity(BlockEntityType<?> blockEntityType, BlockPos p_155630_, BlockState p_155631_) {
        super(blockEntityType, p_155630_, p_155631_);
    }

    @Override
    public void define() {
        this.blockEntityData.define(ADDRESS_ITEM,ItemStack.EMPTY);
        this.blockEntityData.define(ENTITY_ID,0);
        this.blockEntityData.define(ERROR_TYPE,0);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        if(p_155245_.contains(ADDRESS_ITEM_STACK)){
            setAddressItem(ItemStack.of(p_155245_.getCompound(ADDRESS_ITEM_STACK)),0);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        if(!this.getAddressItem(0).isEmpty()) {
            p_187471_.put(ADDRESS_ITEM_STACK,getAddressItem(0).save(new CompoundTag()));
        }
    }

    @Override
    public void updateAddressItem(ItemStack itemStack, int num) {
        if(!getAddressItem(0).isEmpty()){
            reset();
        }
        if(itemStack.isEmpty()) return;
        setAddressItem(itemStack);
        blockChange();
        setChanged();
    }

    @Override
    public boolean isValidAddressItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof AddressItem
                && itemStack.getOrCreateTag().contains(AddressItemInner.ADDRESS_ENTITY_ID);
    }

    @Override
    public void unValidItem(ItemStack itemStack, int num) {
        popAddressItem(0);
    }

    public void reset(){
        setProxyTargetID(0);
        setError(0);
        dropItem(getAddressItem(0));
        setAddressItem(ItemStack.EMPTY);
        setChanged();
    }

    public void dropItem(ItemStack itemStack){
        Containers.dropItemStack(this.level,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ(),itemStack);
    }

    @Override
    public ItemStack getAddressItem(int num) {
        return this.blockEntityData.get(ADDRESS_ITEM);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public UUID getAddress(ItemStack itemStack, @org.jetbrains.annotations.Nullable Integer num) {
        if(!(itemStack.getItem() instanceof AddressItem)) return null;
        CompoundTag tag = itemStack.getTag();
        if(tag == null || !tag.contains(ADDRESS_ENTITY_ID)){
            unValidItem(itemStack,0);
            return null;
        }
        return tag.getUUID(ADDRESS_ENTITY_ID);
    }

    public void setAddressItem(ItemStack itemStack) {
        this.blockEntityData.set(ADDRESS_ITEM,itemStack);
    }

    public void tick(Level level ,BlockPos pos ,BlockState state){
        if(level.isClientSide) return;
        if(isProxyNull()){
            proxyTarget = null;
            setProxyTargetID(0);
            blockChange();
        }

        if(this.blockEntityData.isDirty()){
            broadCastData(false);
            this.level.updateNeighborsAt(this.getBlockPos(),this.getBlockState().getBlock());
        }
    }

    public void setProxyTargetID(int num){
        this.blockEntityData.set(ENTITY_ID,num);
    }

    @Nullable
    public Integer getProxyTargetID(){
        Integer integer = this.blockEntityData.get(ENTITY_ID);
        return integer != 0 ? integer : null;
    }

    @Nullable
    public T getProxyTarget(){
        return this.proxyTarget;
    }

    public void setError(Integer num){
        this.blockEntityData.set(ERROR_TYPE,num);
    }

    public Integer ErrorType(){
        return this.blockEntityData.get(ERROR_TYPE);
    }

    public abstract boolean isProxyNull();

    public abstract void blockChange();



    @Override
    public Component messages() {
        MutableComponent component = Component.empty();
        Component proxyTarget = Component.translatable("none");
        Integer proxyTargetID = getProxyTargetID();
        if(proxyTargetID != null){
            Entity entity = Minecraft.getInstance().level.getEntity(proxyTargetID);
            if(entity != null && entity.isAlive())
                proxyTarget = entity.getDisplayName();
        }

        MutableComponent addressComponent = Component.translatable("redstonehooker.tab.proxy_target").append(proxyTarget);
        component.append(addressComponent);
        if(ErrorType() != 0){
            component.append(Component.translatable("redstonehooker.tab.error")
                    .append(Component.translatable("redstonehooker.address_error_type_" + ErrorType())));
        }
        return component;
    }

    @Override
    public boolean shouldShowMessages() {
        return RedstoneHooker.ShouldShow(this.worldPosition);
    }
}
