package com.automaticalechoes.redstonehooker.common.blockentity;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
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
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ContainerProxyBlockEntity extends DataBlockEntity implements AddressItemInner<BlockPos> , MessagesPreviewable {
    private static final EntityDataAccessor<ItemStack> ADDRESS_ITEM = SynchedBlockEntityData.defineId(ContainerProxyBlockEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> NULL_TYPE =  SynchedBlockEntityData.defineId(ContainerProxyBlockEntity.class,EntityDataSerializers.INT);
    @Nullable private BlockPos proxyTargetPos;
    @Nullable private BlockEntity proxyTarget = null;
    public ContainerProxyBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegister.CONTAINER_PROXY_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    public void define() {
        this.blockEntityData.define(ADDRESS_ITEM,ItemStack.EMPTY);
        this.blockEntityData.define(NULL_TYPE,0);
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
    public BlockEntity getProxyTarget() {
        return isProxyNull() ? this : proxyTarget;
    }

    public boolean isProxyNull(){
        return this.proxyTarget == null || this.proxyTarget.isRemoved();
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if(isProxyNull()){
            if(getAddressItem(0) != ItemStack.EMPTY)
                blockChange(pos);
        }

        if(this.blockEntityData.isDirty()){
            broadCastData(false);
            this.level.updateNeighborsAt(this.getBlockPos(),this.getBlockState().getBlock());
        }
    }

    public void blockChange(BlockPos pos){
        this.proxyTargetPos = getAddress(0);
        if(proxyTargetPos == null) {
            this.blockEntityData.set(NULL_TYPE,1);
            return;
        }

        BlockEntity vanillaBlockEntity = Proxys.getVanillaBlockEntity(this.level, proxyTargetPos);

        if(vanillaBlockEntity == null){
            this.blockEntityData.set(NULL_TYPE,2);
            return;
        }else if(vanillaBlockEntity instanceof ContainerProxyBlockEntity){
            this.blockEntityData.set(NULL_TYPE,3);
            return;
        }

        this.proxyTarget = vanillaBlockEntity;
        if(level instanceof ServerLevel serverLevel){
            serverLevel.getPoiManager().getType(proxyTargetPos)
                    .ifPresent(poiTypeHolder -> serverLevel.getPoiManager().add(pos, poiTypeHolder));
        }
        this.setChanged();
    }

    public void reset(){
        if(level instanceof ServerLevel serverLevel && proxyTargetPos != null){
            serverLevel.getPoiManager().getType(this.worldPosition)
                    .ifPresent((holder) ->serverLevel.getPoiManager().remove(this.worldPosition));
        }
        this.proxyTarget = null;
        this.proxyTargetPos = null;
        this.blockEntityData.set(NULL_TYPE,0);
        if(getAddressItem(0) != ItemStack.EMPTY){
            Containers.dropItemStack(this.level,this.worldPosition.getX(),this.worldPosition.getY(),this.worldPosition.getZ(),getAddressItem(0));
            setAddressItem(ItemStack.EMPTY);
        }
        this.setChanged();
    }

    public void setAddressItem(ItemStack item){
        this.blockEntityData.set(ADDRESS_ITEM,item);
    }

    public void onRemove(){
        reset();
    }

    @Override
    public void updateAddressItem(ItemStack itemStack, int num) {
        if(proxyTarget != null){
            reset();
        }
        if(itemStack.isEmpty()) return;
        setAddressItem(itemStack);
        blockChange(this.worldPosition);
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

    @Override
    public Component messages() {
        MutableComponent component = Component.empty();
        BlockPos address = getAddress(0);
        MutableComponent addressComponent = Component.translatable("address.tab").append(address != null ? address.getCenter().toString() : "none");
        component.append(addressComponent);
        if(this.blockEntityData.get(NULL_TYPE) != 0){
            component.append(Component.translatable("address_null_type_" + this.blockEntityData.get(NULL_TYPE)));
        }
        return component;
    }

    @Override
    public boolean shouldShowMessages() {
        return RedstoneHooker.ShouldShow();
    }
}
