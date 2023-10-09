package com.automaticalechoes.redstonehooker.common.blockentity;

import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import com.automaticalechoes.redstonehooker.api.hooker.AddressListenerManager;
import com.automaticalechoes.redstonehooker.common.item.AddressItem;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class AnisotropicSignalBlockEntity extends DataBlockEntity implements AddressItemInner<BlockPos> {
    private final NonNullList<ItemStack> addressItems = NonNullList.withSize(6,ItemStack.EMPTY);
    private static final EntityDataAccessor<Byte> ADDRESS_IN = SynchedBlockEntityData.defineId(AnisotropicSignalBlockEntity.class, EntityDataSerializers.BYTE);
    public AnisotropicSignalBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegister.ANISOTROPIC_SIGNAL_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    public void define() {
        this.blockEntityData.define(ADDRESS_IN,(byte)0);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        ContainerHelper.loadAllItems(p_155245_.getCompound("items"),addressItems);
        reLoadAddressIn();
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        p_187471_.put("items", ContainerHelper.saveAllItems(new CompoundTag(),addressItems));
    }

    public int getDirectionSignal(Direction direction){
        return getSignal(direction.get3DDataValue());
    }

    public int getSignal(int num){
        int signal = 0;
        ItemStack itemStack = addressItems.get(num);
        if(isValidAddressItem(itemStack)){
            BlockPos addressPos = getAddress(num);
            Integer code = getCode(num);
            if(addressPos != null && code != null){
                BlockState blockState = level.getBlockState(addressPos);
                signal = blockState.getBlock() instanceof RedStoneWireBlock ?
                        blockState.getValue(BlockStateProperties.POWER) : blockState.getSignal(level,addressPos,Direction.from3DDataValue(code));
            }
        }
        return signal;
    }

    @Override
    public void updateAddressItem(ItemStack itemStack, int num) {
        if(level.isClientSide())return;
        if(!addressItems.get(num).isEmpty()){
            AddressListenerManager.removePosListener(getAddress(num),this);
            dropItem(addressItems.get(num));
            addressItems.set(num,ItemStack.EMPTY);
            setChanged();
        }
        if(itemStack.isEmpty()) return;
        addressItems.set(num,itemStack);
        AddressListenerManager.addPosListener(getAddress(num),this);
        setChanged();
    }

    public int[] allSignal(){
        int[] ints = new int[6];
        for (Direction value : Direction.values()) {
            int dDataValue = value.get3DDataValue();
            ints[dDataValue] = getSignal(dDataValue);
        }
        return ints;
    }

    @Override
    public boolean isValidAddressItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof AddressItem
                && itemStack.getOrCreateTag().contains(AddressItemInner.ADDRESS_POS)
                && itemStack.getOrCreateTag().contains(AddressItemInner.ADDRESS_CODE);
    }

    @Override
    public void unValidItem(ItemStack itemStack, int num) {
        popAddressItem(num);
    }

    @Override
    public ItemStack getAddressItem(int num) {
        return addressItems.get(num);
    }

    @Override
    public void setRemoved() {
        removeAllListener();
        super.setRemoved();
    }

    public void onRemove(){
        removeAllListener();
        dropAll();
    }

    public void dropItem(ItemStack itemStack){
        Containers.dropItemStack(this.level,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ(),itemStack);
    }

    public void dropAll(){
        Containers.dropContents(this.level,worldPosition,addressItems);
    }

    public void addAllListener(){
        if(level.isClientSide) return;
        addressItems.stream().filter(this::isValidAddressItem)
                .forEach((ItemStack itemStack) ->  AddressListenerManager.addPosListener(getAddress(itemStack,null), this));
    }

    public void removeAllListener(){
        if(level.isClientSide) return;
        addressItems.stream().filter(this::isValidAddressItem)
                .forEach((ItemStack itemStack) ->  AddressListenerManager.removePosListener(getAddress(itemStack,null), this));
    }

    public void reLoadAddressIn(){
        byte addressIn = 0;
        for (int i = 0; i < addressItems.size(); i++) {
            if(isValidAddressItem(addressItems.get(i))) {
                addressIn = (byte) (addressIn | ( 1<< i));
            }
        }
        this.blockEntityData.set(ADDRESS_IN,addressIn);
        if(level !=  null){
            broadCastData(false);
            this.level.updateNeighborsAt(this.getBlockPos(),this.getBlockState().getBlock());
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        reLoadAddressIn();
    }

    @Override
    public void setLevel(Level p_155231_) {
        super.setLevel(p_155231_);
        addAllListener();
    }

    @Nullable
    @Override
    public BlockPos getAddress(ItemStack itemStack, @Nullable Integer num) {
        if(!(itemStack.getItem() instanceof AddressItem)) return null;
        CompoundTag tag = itemStack.getTag();
        if(tag == null || !tag.contains(ADDRESS_POS)){
            if(num != null) unValidItem(itemStack,num);
            return null;
        }
        long aLong = tag.getLong(ADDRESS_POS);
        return BlockPos.of(aLong);
    }
}
