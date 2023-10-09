package com.automaticalechoes.redstonehooker.common.item;

import com.automaticalechoes.redstonehooker.register.ItemRegister;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class AddressNameTagItem extends NameTagItem {
    public AddressNameTagItem(Properties p_42952_) {
        super(p_42952_);
    }

    public UseAnim getUseAnimation(ItemStack p_43105_) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(ItemStack p_43107_) {
        return 30;
    }

    public InteractionResultHolder<ItemStack> use(Level p_43099_, Player p_43100_, InteractionHand p_43101_) {
        ItemStack itemstack = p_43100_.getItemInHand(p_43101_);
        if(itemstack.hasCustomHoverName() && itemstack.getHoverName().getString().equals("self")){
            p_43100_.startUsingItem(p_43101_);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }


    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        if(p_41411_ instanceof ServerPlayer player && p_41409_.getHoverName().getString().equals("self")){
            ItemStack defaultInstance = ItemRegister.ADDRESS_ITEM.get().getDefaultInstance();
            AddressItem.putEntityAddress(defaultInstance,p_41411_.getUUID(),player.getName());
            return defaultInstance;
        }
        return p_41409_;
    }

    public InteractionResult interactLivingEntity(ItemStack p_42954_, Player p_42955_, LivingEntity p_42956_, InteractionHand p_42957_) {
        if (p_42954_.hasCustomHoverName() && !p_42954_.getHoverName().getString().equals("self") && !(p_42956_ instanceof Player)) {
            if (!p_42955_.level().isClientSide && p_42956_.isAlive()) {
                p_42956_.setCustomName(p_42954_.getHoverName());
                if (p_42956_ instanceof Mob) {
                    ((Mob)p_42956_).setPersistenceRequired();
                }

                p_42954_.shrink(1);
                ItemStack defaultInstance = ItemRegister.ADDRESS_ITEM.get().getDefaultInstance();
                AddressItem.putEntityAddress(defaultInstance,p_42956_.getUUID(),p_42954_.getHoverName());
                Containers.dropItemStack(p_42955_.level(),p_42956_.getX(),p_42956_.getY(),p_42956_.getZ(),defaultInstance);
            }

            return InteractionResult.sidedSuccess(p_42955_.level().isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}
