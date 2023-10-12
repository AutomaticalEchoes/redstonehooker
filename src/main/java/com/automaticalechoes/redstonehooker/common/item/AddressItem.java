package com.automaticalechoes.redstonehooker.common.item;

import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class AddressItem extends Item {
    public static String ADDRESS_TYPE = AddressItemInner.ADDRESS_TYPE;
    public static String ADDRESS_POS = AddressItemInner.ADDRESS_POS;
    public static String ADDRESS_CODE = AddressItemInner.ADDRESS_CODE;
    public static String ADDRESS_ENTITY_ID = AddressItemInner.ADDRESS_ENTITY_ID;
    public static String ADDRESS_ENTITY_CUSTOM_NAME = AddressItemInner.ADDRESS_ENTITY_CUSTOM_NAME;
    public static String ADDRESS_ITEM_COLOR = AddressItemInner.ADDRESS_ITEM_COLOR;
    public static String IS_PLAYER = AddressItemInner.IS_PLAYER;
    public AddressItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        if(p_41427_.getLevel() instanceof ServerLevel serverLevel
                && p_41427_.getHand() == InteractionHand.MAIN_HAND
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
        String message = null;
        if(tag.contains(ADDRESS_TYPE)) {
            switch (tag.getInt(ADDRESS_TYPE)) {
                case 0 -> { message = "type.block_pos";
//            BlockPos proxyTargetPos = BlockPos.of(p_41421_.getTag().getLong(ADDRESS_POS));
//            Direction direction = Direction.from3DDataValue(p_41421_.getTag().getInt(ADDRESS_CODE));
//            p_41423_.add(Component.translatable(proxyTargetPos.getCenter().toString()));
//            p_41423_.add(Component.translatable(direction.getName()));
                }
                case 1 -> { message = "type.entity";
//            UUID uuid = tag.getUUID(ADDRESS_ENTITY_ID);
//            p_41423_.add(Component.translatable(uuid.toString()));
//            String string = tag.getString(ADDRESS_ENTITY_CUSTOM_NAME);
//            p_41423_.add(Component.translatable(string));
                }
            }
        }

        Optional.ofNullable(message).ifPresent(s -> {
            p_41423_.add(Component.empty()
                    .append(Component.translatable("tab.type"))
                    .append(Component.translatable(s)));
        });
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        CompoundTag tag = p_41458_.getOrCreateTag();
        if(tag.contains(ADDRESS_ENTITY_ID) && tag.contains(ADDRESS_ENTITY_CUSTOM_NAME)){
            String string = tag.getString(ADDRESS_ENTITY_CUSTOM_NAME);
            return Component.empty()
                    .append(Component.translatable("tab.proxy_target").withStyle(Style.EMPTY.withColor(getTypeColor(p_41458_) | 0x8F0C8F)))
                    .append(Component.translatable(string).withStyle(Style.EMPTY.withColor(getColor(p_41458_))));
        }else if(tag.contains(ADDRESS_POS) && tag.contains(ADDRESS_CODE)){
            BlockPos proxyTargetPos = BlockPos.of(tag.getLong(ADDRESS_POS));
            return Component.empty()
                    .append(Component.translatable("tab.address").withStyle(Style.EMPTY.withColor(getTypeColor(p_41458_) | 0x8F0C8F)))
                    .append(Component.translatable(proxyTargetPos.getCenter().toString()).withStyle(Style.EMPTY.withColor(getColor(p_41458_))));
        }else {
            return super.getName(p_41458_);
        }
    }

    public static boolean putAddress(ItemStack itemStack, ServerLevel serverLevel , BlockPos pos, int code){
        CompoundTag tag = itemStack.getOrCreateTag();
        if(!canPutAddress(tag)) return false;
        long l = pos.asLong();
        tag.putLong(ADDRESS_POS,l);
        tag.putInt(ADDRESS_CODE,code);
        tag.putInt(ADDRESS_ITEM_COLOR,Proxys.getRGBFromBlockPos(pos));
        tag.putInt(ADDRESS_TYPE,0);
        return true;
    }

    public static boolean putEntityAddress(ItemStack itemStack, UUID entityID, Component customName,boolean isPlayer){
        CompoundTag tag = itemStack.getOrCreateTag();
        if(!canPutAddress(tag)) return false;
        tag.putUUID(ADDRESS_ENTITY_ID,entityID);
        tag.putString(ADDRESS_ENTITY_CUSTOM_NAME,customName.getString());
        tag.putInt(ADDRESS_ITEM_COLOR,Proxys.getRGBFromUUID(entityID));
        tag.putBoolean(IS_PLAYER,isPlayer);
        tag.putInt(ADDRESS_TYPE,1);
        return true;
    }

    public static boolean canPutAddress(CompoundTag tag){
       return !(tag.contains(ADDRESS_POS) || tag.contains(ADDRESS_CODE)
               || tag.contains(ADDRESS_ENTITY_CUSTOM_NAME) || tag.contains(ADDRESS_ENTITY_ID)) ;
    }

    public static int getColor(ItemStack itemStack){
        CompoundTag tag = itemStack.getTag();
        if(tag == null || !tag.contains(ADDRESS_ITEM_COLOR)){
            return -1;
        }
        return tag.getInt(ADDRESS_ITEM_COLOR);
    }

    public static int getTypeColor(ItemStack itemStack){
        CompoundTag tag = itemStack.getTag();
        if(tag == null || !tag.contains(ADDRESS_TYPE)){
            return  0x2e221c;
        }
        return switch (itemStack.getOrCreateTag().getInt(ADDRESS_TYPE)){
            case 0 -> 0x371b42;
            default -> 0x2e221c;
        };
    }
}
