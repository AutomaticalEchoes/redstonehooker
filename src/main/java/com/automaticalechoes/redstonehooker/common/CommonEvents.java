package com.automaticalechoes.redstonehooker.common;


import com.automaticalechoes.redstonehooker.event.server.OnListenPosChange;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void onListenPosChange(OnListenPosChange event){
        event.getPosListeners().forEach(BlockEntity::setChanged);
    }

}
