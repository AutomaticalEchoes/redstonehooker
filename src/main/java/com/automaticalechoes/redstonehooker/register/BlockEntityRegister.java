package com.automaticalechoes.redstonehooker.register;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.common.blockentity.AnisotropicSignalBlockEntity;
import com.automaticalechoes.redstonehooker.common.blockentity.InventoryEntityProxyBlockEntity;
import com.automaticalechoes.redstonehooker.common.blockentity.ContainerProxyBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RedstoneHooker.MODID);
    public static final RegistryObject<BlockEntityType<ContainerProxyBlockEntity>> CONTAINER_PROXY_BLOCK_ENTITY = BLOCK_ENTITIES.register("container_proxy_block_entity",
            () -> BlockEntityType.Builder.of(ContainerProxyBlockEntity::new, BlockRegister.CONTAINER_PROXY_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<AnisotropicSignalBlockEntity>> ANISOTROPIC_SIGNAL_BLOCK_ENTITY = BLOCK_ENTITIES.register("anisotropic_signal_block_entity",
            () -> BlockEntityType.Builder.of(AnisotropicSignalBlockEntity::new, BlockRegister.ANISOTROPIC_SIGNAL_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<InventoryEntityProxyBlockEntity>> INVENTORY_ENTITY_PROXY_BLOCK_ENTITY = BLOCK_ENTITIES.register("inventory_entity_proxy_block_entity",
            () -> BlockEntityType.Builder.of(InventoryEntityProxyBlockEntity::new, BlockRegister.INVENTORY_ENTITY_PROXY_BLOCK.get()).build(null));
}
