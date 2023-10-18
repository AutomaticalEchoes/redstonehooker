package com.automaticalechoes.redstonehooker.register;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.common.block.AddressInner.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RedstoneHooker.MODID);

    public static final RegistryObject<ArmorProxyBlock> ARMOR_PROXY_BLOCK  = BLOCKS.register("armor_proxy_block",() -> new ArmorProxyBlock(BlockBehaviour
            .Properties.of()
            .lightLevel(value -> 7)
            .strength(2.5F)
            .noOcclusion()
            .sound(SoundType.WOOD)
            .isViewBlocking(BlockRegister::never)));

    public static final RegistryObject<BlockEntityProxyBlock> BLOCK_ENTITY_PROXY_BLOCK  = BLOCKS.register("block_entity_proxy_block",() -> new BlockEntityProxyBlock(BlockBehaviour
            .Properties.of()
            .lightLevel(value -> 7)
            .strength(2.5F)
            .noOcclusion()
            .sound(SoundType.WOOD)
            .isViewBlocking(BlockRegister::never)));

    public static final RegistryObject<GatewayProxyBlock> GATEWAY_PROXY_BLOCK  = BLOCKS.register("gateway_proxy_block",() -> new GatewayProxyBlock(BlockBehaviour
            .Properties.of()
            .lightLevel(value -> 7)
            .strength(3.5F)
            .noOcclusion()
            .sound(SoundType.WOOD)
            .isViewBlocking(BlockRegister::never)));

    public static final RegistryObject<AnisotropicSignalBlock> ANISOTROPIC_SIGNAL_BLOCK  = BLOCKS.register("anisotropic_signal_block",() -> new AnisotropicSignalBlock(BlockBehaviour
            .Properties.of()
            .isRedstoneConductor(BlockRegister::never)
            .noOcclusion()
            .sound(SoundType.WOOD)
            .isViewBlocking(BlockRegister::never)));

    public static final RegistryObject<InventoryEntityProxyBlock> INVENTORY_ENTITY_PROXY_BLOCK  = BLOCKS.register("inventory_entity_proxy_block",() -> new InventoryEntityProxyBlock(BlockBehaviour
            .Properties.of()
            .isRedstoneConductor(BlockRegister::never)
            .strength(2.5F)
            .noOcclusion()
            .sound(SoundType.WOOD)
            .isViewBlocking(BlockRegister::never)));

    public static final RegistryObject<Block> FAKE_NETHER_CAULDRON = BLOCKS.register("fake_nether_cauldron", () -> new Block(BlockBehaviour.Properties.of()));

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
}
