package com.automaticalechoes.redstonehooker.register;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.common.blockentity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RedstoneHooker.MODID);
    public static final RegistryObject<BlockEntityType<ProxyBlockEntity>> BLOCK_ENTITY_PROXY_BLOCK_ENTITY = BLOCK_ENTITIES.register("block_entity_proxy_block_entity",
            () -> BlockEntityType.Builder.of(ProxyBlockEntity::new, BlockRegister.BLOCK_ENTITY_PROXY_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<AnisotropicSignalBlockEntity>> ANISOTROPIC_SIGNAL_BLOCK_ENTITY = BLOCK_ENTITIES.register("anisotropic_signal_block_entity",
            () -> BlockEntityType.Builder.of(AnisotropicSignalBlockEntity::new, BlockRegister.ANISOTROPIC_SIGNAL_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<InventoryEntityProxyBlockEntity>> INVENTORY_ENTITY_PROXY_BLOCK_ENTITY = BLOCK_ENTITIES.register("inventory_entity_proxy_block_entity",
            () -> BlockEntityType.Builder.of(InventoryEntityProxyBlockEntity::new, BlockRegister.INVENTORY_ENTITY_PROXY_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ArmorProxyBlockEntity>> ARMOR_PROXY_BLOCK_ENTITY = BLOCK_ENTITIES.register("armor_entity_proxy_block_entity",
            () -> BlockEntityType.Builder.of(ArmorProxyBlockEntity::new, BlockRegister.ARMOR_PROXY_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<GatewayProxyBlockEntity>> GATEWAY_PROXY_BLOCK_ENTITY = BLOCK_ENTITIES.register("gateway_proxy_block_entity",
            () -> BlockEntityType.Builder.of(GatewayProxyBlockEntity::new, BlockRegister.GATEWAY_PROXY_BLOCK.get()).build(null));
}
