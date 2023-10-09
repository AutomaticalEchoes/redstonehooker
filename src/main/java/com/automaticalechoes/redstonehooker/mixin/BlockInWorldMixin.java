package com.automaticalechoes.redstonehooker.mixin;

import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockInWorld.class)
public class BlockInWorldMixin {
    @Shadow
    @Final
    LevelReader level;

    @Shadow
    @Final
    BlockPos pos;

    @Inject(method = "getEntity", at = @At(value = "RETURN"),cancellable = true)
    public void getEntity(CallbackInfoReturnable<BlockEntity> callbackInfoReturnable) {
        if(callbackInfoReturnable.getReturnValue() != null)
        callbackInfoReturnable.setReturnValue(Proxys.getVanillaBlockEntity((Level) level,pos));
    }
}
