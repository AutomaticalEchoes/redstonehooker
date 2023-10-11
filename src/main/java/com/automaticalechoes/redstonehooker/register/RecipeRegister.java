package com.automaticalechoes.redstonehooker.register;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.common.recipe.SmithingPutTagRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RecipeRegister {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RedstoneHooker.MODID);
    public static final RegistryObject<RecipeSerializer<SmithingPutTagRecipe>> SMITHING_PUT_TAG = RECIPES.register("smithing_put_tag", SmithingPutTagRecipe.Serializer::new);
}
