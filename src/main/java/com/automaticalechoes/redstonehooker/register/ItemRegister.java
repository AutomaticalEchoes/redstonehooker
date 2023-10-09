package com.automaticalechoes.redstonehooker.register;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.common.item.AddressItem;
import com.automaticalechoes.redstonehooker.common.item.AddressNameTagItem;
import com.automaticalechoes.redstonehooker.common.item.HookerAdjustItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RedstoneHooker.MODID);
    public static final ArrayList<RegistryObject<? extends Item>> MOD_ITEMS = new ArrayList<>();
    public static final RegistryObject<BlockItem> CONTAINER_PROXY_BLOCK_ITEM = Register("container_proxy_block_item", () -> new BlockItem(BlockRegister.CONTAINER_PROXY_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<BlockItem> ANISOTROPIC_SIGNAL_BLOCK_ITEM = Register("anisotropic_signal_block_item", () -> new BlockItem(BlockRegister.ANISOTROPIC_SIGNAL_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<BlockItem> INVENTORY_ENTITY_PROXY_BLOCK_ITEM = Register("inventory_entity_proxy_block_item", () -> new BlockItem(BlockRegister.INVENTORY_ENTITY_PROXY_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<HookerAdjustItem> HOOKER_ADJUST = Register("hooker_adjust_item", () -> new HookerAdjustItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<AddressItem> ADDRESS_ITEM = Register("address_item",() -> new AddressItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<AddressNameTagItem> ADDRESS_NAME_TAG_ITEM = Register("address_name_tag_item",() -> new AddressNameTagItem(new Item.Properties().stacksTo(1)));
    public static <T extends Item> RegistryObject<T> Register(String key, Supplier<T> supplier){
        RegistryObject<T> register = ITEMS.register(key, supplier);
        MOD_ITEMS.add(register);
        return register;
    }
}
