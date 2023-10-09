package com.automaticalechoes.redstonehooker.mixin;

import com.automaticalechoes.redstonehooker.api.hooker.AddressListenerManager;
import com.automaticalechoes.redstonehooker.event.server.OnListenPosChange;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;


@Mixin(CollectingNeighborUpdater.class)
public class CollectingNeighborUpdaterMixin {

    @Inject(method = "updateNeighborsAtExceptFromFacing", at = {@At("RETURN")})
    public void updateNeighborsAtExceptFromFacing(BlockPos p_230657_, Block p_230658_, @Nullable Direction p_230659_, CallbackInfo info) {
        if(AddressListenerManager.posContainersListener(p_230657_)){
            MinecraftForge.EVENT_BUS.post(new OnListenPosChange(p_230657_));
        }

    }
}
