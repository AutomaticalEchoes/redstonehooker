package com.automaticalechoes.redstonehooker.common.item;

import com.automaticalechoes.redstonehooker.common.entity.AddressTagProjectile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class ThrowableTag extends ArrowItem implements Vanishable {
    public ThrowableTag(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public AbstractArrow createArrow(Level p_40513_, ItemStack p_40514_, LivingEntity p_40515_) {
        return new AddressTagProjectile(p_40515_,p_40513_,p_40514_);
    }
//
//    @Override
//    public void releaseUsing(ItemStack p_41412_, Level p_41413_, LivingEntity p_41414_, int p_41415_) {
//        int i = Math.max( (getUseDuration(p_41412_) - p_41415_) / 10, 1);
//        int f = Math.min(6,i);
//        if(p_41413_ instanceof ServerLevel serverLevel){
//            DipolarTubeProjectile dipolarTubeProjectile = DipolarUtils.makeProjectile(serverLevel,p_41412_,p_41414_);
//            if(i >= 2){
//                dipolarTubeProjectile.setTurn(true);
//            }else {
//                dipolarTubeProjectile.setVertical(false);
//            }
//            dipolarTubeProjectile.shootFromRotation(p_41414_, p_41414_.getXRot(), p_41414_.getYRot(), -4.0F * (6 - f) - 2.0F, 0.5F * f, 1.0F);
//            serverLevel.addFreshEntity(dipolarTubeProjectile);
//        }
//        if (p_41414_ instanceof Player player && player.getAbilities().instabuild) return;
//
//        p_41412_.shrink(1);
//    }
//
//    public int getUseDuration(ItemStack p_43001_) {
//        return 72000;
//    }
//

}
