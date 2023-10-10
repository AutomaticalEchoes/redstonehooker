package com.automaticalechoes.redstonehooker.common.item;

import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HookerAdjustItem extends Item {
    public HookerAdjustItem(Properties p_40566_) {
        super(p_40566_);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_40581_) {
        BlockEntity blockEntity = Proxys.getVanillaBlockEntity(p_40581_.getLevel(),p_40581_.getClickedPos());
        int dDataValue = p_40581_.getClickedFace().getOpposite().get3DDataValue();
        boolean isDo = false;
        if(p_40581_.getLevel() instanceof ServerLevel serverLevel && p_40581_.getPlayer() instanceof ServerPlayer serverPlayer){
            if(serverPlayer.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof AddressItem
                    && AddressItem.putAddress(serverPlayer.getItemInHand(InteractionHand.OFF_HAND), serverLevel, p_40581_.getClickedPos(),dDataValue)){
                isDo = true;
            }else if(blockEntity instanceof AddressItemInner addressItemInner && addressItemInner.getAddressItem(dDataValue) != ItemStack.EMPTY){
                addressItemInner.popAddressItem(dDataValue);
                isDo = true;
            }
        }

        return isDo ? InteractionResult.SUCCESS : super.useOn(p_40581_);
    }

}
