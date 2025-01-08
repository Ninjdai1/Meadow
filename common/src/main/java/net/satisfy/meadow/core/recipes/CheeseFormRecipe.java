package net.satisfy.meadow.core.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.satisfy.meadow.core.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CheeseFormRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient bucket;
    private final Ingredient ingredient;
    private final ItemStack result;

    public CheeseFormRecipe(ResourceLocation id, Ingredient bucket, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.bucket = bucket;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        NonNullList<Ingredient> ingredients = getIngredients();
        List<ItemStack> items = new ArrayList<>(List.of(inventory.getItem(1), inventory.getItem(2)));
        for (Ingredient ingredient : ingredients) {
            boolean matches = false;
            for (ItemStack stack : items) {
                if (ingredient.test(stack)) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(Container inventory, RegistryAccess registryManager) {
        return this.result.copy();
    }


    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> defaultedList = NonNullList.create();
        defaultedList.add(this.bucket);
        defaultedList.add(this.ingredient);
        return defaultedList;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public ItemStack getResultItem() {
        return getResultItem(null);
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess registryManager) {
        return result;
    }


    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.CHEESE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.CHEESE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<CheeseFormRecipe> {

        @Override
        public @NotNull CheeseFormRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonElement jsonResultElement = json.get("result");
            ItemStack resultItem = GsonHelper.convertToItem(jsonResultElement, jsonResultElement.getAsString()).getDefaultInstance();

            JsonObject jsonBucketObject = json.getAsJsonObject("bucket");
            Ingredient bucket = Ingredient.fromJson(jsonBucketObject);

            JsonObject jsonIngredientObject = json.getAsJsonObject("ingredient");
            Ingredient ingredient = Ingredient.fromJson(jsonIngredientObject);

            return new CheeseFormRecipe(id, bucket, ingredient, resultItem);
        }

        @Override
        public @NotNull CheeseFormRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf packetData) {
            Ingredient bucket = Ingredient.fromNetwork(packetData);
            Ingredient input = Ingredient.fromNetwork(packetData);
            ItemStack output = packetData.readItem();
            return new CheeseFormRecipe(id, bucket, input, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, CheeseFormRecipe recipe) {
            recipe.getIngredients().forEach(ingredient -> ingredient.toNetwork(friendlyByteBuf));
            friendlyByteBuf.writeItem(recipe.result);
        }
    }

}
