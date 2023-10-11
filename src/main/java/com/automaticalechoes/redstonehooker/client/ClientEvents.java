package com.automaticalechoes.redstonehooker.client;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void OnItemToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem armorItem
                && armorItem.getType() == ArmorItem.Type.HELMET
                && event.getItemStack().getOrCreateTag().contains(RedstoneHooker.MODID))
            event.getToolTip().add(Component.translatable(RedstoneHooker.MODID).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

}
