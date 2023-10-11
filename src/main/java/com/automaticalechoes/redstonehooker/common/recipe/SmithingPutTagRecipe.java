package com.automaticalechoes.redstonehooker.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.stream.Stream;

public class SmithingPutTagRecipe implements SmithingRecipe {
    private final ResourceLocation id;
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;
    final String tagName;
    final CompoundTag tagValue;

    public SmithingPutTagRecipe(ResourceLocation p_267235_, Ingredient p_267298_, Ingredient p_266862_, Ingredient p_267050_, String tagName, CompoundTag tagValue) {
        this.id = p_267235_;
        this.template = p_267298_;
        this.base = p_266862_;
        this.addition = p_267050_;
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public boolean matches(Container p_267224_, Level p_266798_) {
        return this.template.test(p_267224_.getItem(0))
                && this.base.test(p_267224_.getItem(1))
                && this.addition.test(p_267224_.getItem(2) )
                && !p_267224_.getItem(1).getOrCreateTag().contains(tagName);
    }

    public ItemStack assemble(Container p_267320_, RegistryAccess p_267280_) {
        ItemStack itemstack = p_267320_.getItem(1);
        if (this.base.test(itemstack)) {
            ItemStack copy = itemstack.copy();
            copy.getOrCreateTag().put(tagName,tagValue);
            return copy;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(RegistryAccess p_266948_) {
        ItemStack itemstack = new ItemStack(Items.IRON_CHESTPLATE);
        Optional<Holder.Reference<TrimPattern>> optional = p_266948_.registryOrThrow(Registries.TRIM_PATTERN).holders().findFirst();
        if (optional.isPresent()) {
            Optional<Holder.Reference<TrimMaterial>> optional1 = p_266948_.registryOrThrow(Registries.TRIM_MATERIAL).getHolder(TrimMaterials.REDSTONE);
            if (optional1.isPresent()) {
                ArmorTrim armortrim = new ArmorTrim(optional1.get(), optional.get());
                ArmorTrim.setTrim(p_266948_, itemstack, armortrim);
            }
        }

        return itemstack;
    }

    public boolean isTemplateIngredient(ItemStack p_266762_) {
        return this.template.test(p_266762_);
    }

    public boolean isBaseIngredient(ItemStack p_266795_) {
        return this.base.test(p_266795_);
    }

    public boolean isAdditionIngredient(ItemStack p_266922_) {
        return this.addition.test(p_266922_);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SMITHING_TRIM;
    }

    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(net.minecraftforge.common.ForgeHooks::hasNoElements);
    }

    public static class Serializer implements RecipeSerializer<SmithingPutTagRecipe> {
        public SmithingPutTagRecipe fromJson(ResourceLocation p_267037_, JsonObject p_267004_) {
            JsonElement template = p_267004_.get("template");
            JsonElement addition = p_267004_.get("addition");
            Ingredient ingredient =  template !=null && !template.isJsonNull() ? Ingredient.fromJson(template) : Ingredient.EMPTY;
            Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getNonNull(p_267004_, "base"));
            Ingredient ingredient2 = addition !=null && !addition.isJsonNull() ?Ingredient.fromJson(addition) : Ingredient.EMPTY;
            String tag_name = GsonHelper.getNonNull(p_267004_, "tag_name").getAsString();
            CompoundTag tag = new CompoundTag();
            try {
                JsonElement tag_value = p_267004_.get("tag_value");
                if(tag_value != null && !tag_value.isJsonNull()){
                    tag = TagParser.parseTag(tag_value.getAsString());
                }
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
            return new SmithingPutTagRecipe(p_267037_, ingredient, ingredient1, ingredient2, tag_name, tag);
        }

        public SmithingPutTagRecipe fromNetwork(ResourceLocation p_267169_, FriendlyByteBuf p_267251_) {
            Ingredient ingredient = Ingredient.fromNetwork(p_267251_);
            Ingredient ingredient1 = Ingredient.fromNetwork(p_267251_);
            Ingredient ingredient2 = Ingredient.fromNetwork(p_267251_);
            String tag_ame = p_267251_.readComponent().getString();
            CompoundTag tag_value = p_267251_.readNbt();
            return new SmithingPutTagRecipe(p_267169_, ingredient, ingredient1, ingredient2, tag_ame, tag_value);
        }

        public void toNetwork(FriendlyByteBuf p_266901_, SmithingPutTagRecipe p_266893_) {
            p_266893_.template.toNetwork(p_266901_);
            p_266893_.base.toNetwork(p_266901_);
            p_266893_.addition.toNetwork(p_266901_);
            p_266901_.writeComponent(Component.translatable(p_266893_.tagName));
            p_266901_.writeNbt(p_266893_.tagValue);
        }
    }
}
