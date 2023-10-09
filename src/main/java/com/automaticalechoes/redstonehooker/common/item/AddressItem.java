package com.automaticalechoes.redstonehooker.common.item;

import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class AddressItem extends Item {
    public static String ADDRESS_POS = AddressItemInner.ADDRESS_POS;
    public static String ADDRESS_CODE = AddressItemInner.ADDRESS_CODE;
    public static String ADDRESS_ENTITY_ID = AddressItemInner.ADDRESS_ENTITY_ID;
    public static String ADDRESS_ENTITY_CUSTOM_NAME = AddressItemInner.ADDRESS_ENTITY_CUSTOM_NAME;
    public AddressItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        if(p_41427_.getLevel() instanceof ServerLevel serverLevel
                && Proxys.getVanillaBlockEntity(serverLevel,p_41427_.getClickedPos()) instanceof AddressItemInner addressItemInner
                && addressItemInner.isValidAddressItem(p_41427_.getItemInHand())){
            addressItemInner.setAddressItem(p_41427_.getItemInHand().split(1),p_41427_.getClickedFace().get3DDataValue());
        }
        return super.useOn(p_41427_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        CompoundTag tag = p_41421_.getTag();
        if(tag == null) return;
        if(tag.contains(ADDRESS_ENTITY_ID) && tag.contains(ADDRESS_ENTITY_CUSTOM_NAME)){
//            UUID uuid = tag.getUUID(ADDRESS_ENTITY_ID);
            String string = tag.getString(ADDRESS_ENTITY_CUSTOM_NAME);
//            p_41423_.add(Component.translatable(uuid.toString()));
            p_41423_.add(Component.translatable(string));
        }else if(tag.contains(ADDRESS_POS) && tag.contains(ADDRESS_CODE)){
            BlockPos proxyTargetPos = BlockPos.of(p_41421_.getTag().getLong(ADDRESS_POS));
            Direction direction = Direction.from3DDataValue(p_41421_.getTag().getInt(ADDRESS_CODE));
            p_41423_.add(Component.translatable(proxyTargetPos.getCenter().toString()));
            p_41423_.add(Component.translatable(direction.getName()));
        }else {
            p_41423_.add(Component.translatable("Address None"));
        }

    }

    public static boolean putAddress(ItemStack itemStack, ServerLevel serverLevel , BlockPos pos, int code){
        CompoundTag tag = itemStack.getOrCreateTag();
        if(!canPutAddress(tag)) return false;
        long l = pos.asLong();
        tag.putLong(ADDRESS_POS,l);
        tag.putInt(ADDRESS_CODE,code);
        return true;
    }

    public static boolean putEntityAddress(ItemStack itemStack, UUID entityID, Component customName){
        CompoundTag tag = itemStack.getOrCreateTag();
        if(!canPutAddress(tag)) return false;
        tag.putUUID(ADDRESS_ENTITY_ID,entityID);
        tag.putString(ADDRESS_ENTITY_CUSTOM_NAME,customName.getString());
        return true;
    }
    public static boolean canPutAddress(CompoundTag tag){
       return !(tag.contains(ADDRESS_POS) || tag.contains(ADDRESS_CODE)
               || tag.contains(ADDRESS_ENTITY_CUSTOM_NAME) || tag.contains(ADDRESS_ENTITY_ID)) ;
    }
}
