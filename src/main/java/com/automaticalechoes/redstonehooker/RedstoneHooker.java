package com.automaticalechoes.redstonehooker;

import com.automaticalechoes.redstonehooker.common.item.HookerAdjustItem;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import com.automaticalechoes.redstonehooker.register.BlockRegister;
import com.automaticalechoes.redstonehooker.register.ItemRegister;
import com.automaticalechoes.redstonehooker.register.RecipeRegister;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RedstoneHooker.MODID)
public class RedstoneHooker
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "redstonehooker";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> REDSTONE_HOOKER = CREATIVE_MODE_TABS.register("redstone_hooker", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("itemGroup"))
            .icon(() -> ItemRegister.HOOKER_ADJUST.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (RegistryObject<? extends Item> modItem : ItemRegister.MOD_ITEMS) {
                    output.accept(modItem.get()); //
                }
            }).build());

    public RedstoneHooker()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        ItemRegister.ITEMS.register(modEventBus);
        BlockRegister.BLOCKS.register(modEventBus);
        BlockEntityRegister.BLOCK_ENTITIES.register(modEventBus);
        RecipeRegister.RECIPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS)
            event.accept(ItemRegister.CONTAINER_PROXY_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public static boolean ShouldShow(){
        LocalPlayer player = Minecraft.getInstance().player;
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof HookerAdjustItem || player.getItemBySlot(EquipmentSlot.HEAD).getOrCreateTag().contains(MODID);
    }

}
