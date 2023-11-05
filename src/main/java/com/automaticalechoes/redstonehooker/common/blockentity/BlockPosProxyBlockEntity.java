package com.automaticalechoes.redstonehooker.common.blockentity;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewable;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class BlockPosProxyBlockEntity<T> extends DataBlockEntity implements AddressItemInner<BlockPos> , MessagesPreviewable {
    private static final EntityDataAccessor<ItemStack> ADDRESS_ITEM = SynchedBlockEntityData.defineId(BlockPosProxyBlockEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> ERROR_TYPE =  SynchedBlockEntityData.defineId(BlockPosProxyBlockEntity.class,EntityDataSerializers.INT);
    @Nullable protected BlockPos proxyTargetPos;
    @Nullable protected T proxyTarget = null;
    public BlockPosProxyBlockEntity(BlockEntityType<?> blockEntityType ,BlockPos p_155229_, BlockState p_155230_) {
        super(blockEntityType, p_155229_, p_155230_);
    }

    @Override
    public void define() {
        this.blockEntityData.define(ADDRESS_ITEM,ItemStack.EMPTY);
        this.blockEntityData.define(ERROR_TYPE,0);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        if(p_155245_.contains(ADDRESS_ITEM_STACK)){
            setAddressItem(ItemStack.of(p_155245_.getCompound(ADDRESS_ITEM_STACK)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        if(this.proxyTarget != null) {
            p_187471_.put(ADDRESS_ITEM_STACK,getAddressItem(0).save(new CompoundTag()));
        }
    }

    @Nullable
    public T getProxyTarget() {
        return proxyTarget;
    }

    public abstract boolean isProxyNull();

    public abstract void blockChange(BlockPos pos);

    public void tick(Level level, BlockPos pos, BlockState state){
        if(!(level instanceof ServerLevel serverLevel)) return;
        if(isProxyNull()){
            if(getAddressItem(0) != ItemStack.EMPTY)
                blockChange(pos);
        }

        if(this.blockEntityData.isDirty()){
            broadCastData(false);
            this.level.updateNeighborsAt(this.getBlockPos(),this.getBlockState().getBlock());
        }
    }

    public void reset(){
        if(level instanceof ServerLevel serverLevel && proxyTargetPos != null){
            serverLevel.getPoiManager().getType(this.worldPosition)
                    .ifPresent((holder) ->serverLevel.getPoiManager().remove(this.worldPosition));
        }
        this.proxyTarget = null;
        this.proxyTargetPos = null;
        setError(0);
        if(getAddressItem(0) != ItemStack.EMPTY){
            Containers.dropItemStack(this.level,this.worldPosition.getX(),this.worldPosition.getY(),this.worldPosition.getZ(),getAddressItem(0));
            setAddressItem(ItemStack.EMPTY);
        }
        this.setChanged();
    }

    public void setAddressItem(ItemStack item){
        this.blockEntityData.set(ADDRESS_ITEM,item);
    }

    @Override
    public void updateAddressItem(ItemStack itemStack, int num) {
        if(!getAddressItem(0).isEmpty()){
            reset();
        }
        if(itemStack.isEmpty()) return;
        setAddressItem(itemStack);
        blockChange(this.worldPosition);
        this.setChanged();
    }

    @Override
    public boolean isValidAddressItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof AddressItem && itemStack.getOrCreateTag().contains(AddressItemInner.ADDRESS_POS);
    }

    @Override
    public void unValidItem(ItemStack itemStack, int num) {
        reset();
    }

    @Override
    public ItemStack getAddressItem(int num) {
        return this.blockEntityData.get(ADDRESS_ITEM);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockPos getAddress(ItemStack itemStack, @org.jetbrains.annotations.Nullable Integer num) {
        if(!(itemStack.getItem() instanceof AddressItem)) return null;
        CompoundTag tag = itemStack.getTag();
        if(tag == null || !tag.contains(ADDRESS_POS)){
            unValidItem(itemStack,0);
            return null;
        }
        long aLong = tag.getLong(ADDRESS_POS);
        return BlockPos.of(aLong);
    }

    public void setError(Integer num){
        this.blockEntityData.set(ERROR_TYPE,num);
    }

    public Integer ErrorType(){
        return this.blockEntityData.get(ERROR_TYPE);
    }

    @Override
    public Component messages() {
        MutableComponent component = Component.empty();
        BlockPos address = getAddress(0);
        MutableComponent addressComponent = Component.translatable("redstonehooker.tab.address").append(address != null ? address.getCenter().toString() : "none");
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
