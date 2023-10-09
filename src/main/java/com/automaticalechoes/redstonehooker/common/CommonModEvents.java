package com.automaticalechoes.redstonehooker.common;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.common.netWork.PacketHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = RedstoneHooker.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    public static SimpleChannel CHANNEL;
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        CHANNEL = PacketHandler.RegisterPacket();
    }

}
