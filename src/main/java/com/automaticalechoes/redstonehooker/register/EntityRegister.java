package com.automaticalechoes.redstonehooker.register;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.common.entity.AddressTagProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegister{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RedstoneHooker.MODID);
    public static final RegistryObject<EntityType<AddressTagProjectile>> ADDRESS_TAG_PROJECTILE = ENTITY_TYPES.register("address_tag_projectile",
            () ->EntityType.Builder.of(AddressTagProjectile::Create, MobCategory.MISC).sized(0.5f,0.5f)
                    .build("address_tag_projectile"));

}
