package com.automaticalechoes.redstonehooker.api.addressItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface AddressItemInner<T> {
    String ADDRESS_ITEM_STACK = "address_item_stack";
    String ADDRESS_CODE = "address_code";
    String ADDRESS_POS = "address_pos";
    String ADDRESS_ENTITY_ID = "address_entity_id";
    String ADDRESS_ENTITY_CUSTOM_NAME = "address_entity_custom_name";
    String ADDRESS_ITEM_COLOR = "address_item_color";
    void updateAddressItem(ItemStack itemStack,int num);

    boolean isValidAddressItem(ItemStack itemStack);

    void unValidItem(ItemStack itemStack,int num);

    ItemStack getAddressItem(int num);

    default void setAddressItem(ItemStack itemStack ,int num){
        if(isValidAddressItem(itemStack)){
            updateAddressItem(itemStack,num);
        }
    }

    default void popAddressItem(int num){
        updateAddressItem(ItemStack.EMPTY,num);
    }

    @Nullable
    default Integer getCode(int num){
        return getCode(getAddressItem(num),num);
    }

    @Nullable
    default Integer getCode(ItemStack itemStack,int num){
        CompoundTag tag = itemStack.getTag();
        if(tag == null || !tag.contains(ADDRESS_CODE)){
            unValidItem(itemStack,num);
            return null;
        }
        return tag.getInt(ADDRESS_CODE);
    }

    @Nullable
    default T getAddress(int num){
        return getAddress(getAddressItem(num),num);
    }

    @Nullable
    T getAddress(ItemStack itemStack, @Nullable Integer num);
}
