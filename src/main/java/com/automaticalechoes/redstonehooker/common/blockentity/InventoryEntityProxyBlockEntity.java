package com.automaticalechoes.redstonehooker.common.blockentity;

import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import com.automaticalechoes.redstonehooker.common.item.AddressItem;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

public class InventoryEntityProxyBlockEntity extends DataBlockEntity implements Container , AddressItemInner<UUID> {
    private static final EntityDataAccessor<ItemStack> ADDRESS_ITEM = SynchedBlockEntityData.defineId(InventoryEntityProxyBlockEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> ENTITY_ID = SynchedBlockEntityData.defineId(InventoryEntityProxyBlockEntity.class,EntityDataSerializers.INT);
    @Nullable
    private InventoryCarrier proxyTarget = null;
    public InventoryEntityProxyBlockEntity(BlockPos p_155630_, BlockState p_155631_) {
        super(BlockEntityRegister.INVENTORY_ENTITY_PROXY_BLOCK_ENTITY.get(), p_155630_, p_155631_);
    }

    @Override
    public void define() {
        this.blockEntityData.define(ADDRESS_ITEM,ItemStack.EMPTY);
        this.blockEntityData.define(ENTITY_ID,0);
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
    public int getContainerSize() {
        return proxyTarget != null? proxyTarget.getInventory().getContainerSize() : 0;
    }

    @Override
    public boolean isEmpty() {
        return proxyTarget == null || proxyTarget.getInventory().isEmpty();
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
    public void updateAddressItem(ItemStack itemStack, int num) {
        if(!getAddressItem(0).isEmpty()){
            reset();
        }
        if(itemStack.isEmpty()) return;
        setAddressItem(itemStack);
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
        if(!(level instanceof ServerLevel serverLevel)) return;
        boolean isProxyNull = getAddressItem(0).isEmpty() || proxyTarget == null || !(proxyTarget instanceof Entity entity) || !entity.isAlive();
        if(isProxyNull){
            proxyTarget = null;
            setProxyTargetID(0);
            if(!this.getAddressItem(0).isEmpty() && getAddress(0) != null
                    && serverLevel.getEntity(getAddress(0)) instanceof InventoryCarrier inventoryCarrier){
                Entity entity = serverLevel.getEntity(getAddress(0));
                this.proxyTarget = inventoryCarrier;
                setProxyTargetID(entity.getId());
            }
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

    public void onRemove(){
        reset();
    }
}
