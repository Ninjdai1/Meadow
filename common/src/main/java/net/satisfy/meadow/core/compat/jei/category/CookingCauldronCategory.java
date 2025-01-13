package net.satisfy.meadow.core.compat.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.meadow.Meadow;
import net.satisfy.meadow.client.gui.CookingCauldronGui;
import net.satisfy.meadow.core.compat.jei.MeadowJEIPlugin;
import net.satisfy.meadow.core.recipes.CookingCauldronRecipe;
import net.satisfy.meadow.core.registry.ObjectRegistry;
import org.joml.Vector2i;
import org.jetbrains.annotations.NotNull;

public class CookingCauldronCategory implements IRecipeCategory<CookingCauldronRecipe> {
    public static final RecipeType<CookingCauldronRecipe> COOKING_CAULDRON = RecipeType.create(Meadow.MOD_ID, "cooking_cauldron", CookingCauldronRecipe.class);
    private static final int WIDTH = 124;
    private static final int HEIGHT = 55;
    private static final int WIDTH_OF = 26;
    private static final int HEIGHT_OF = 10;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable burnicon;
    private final IDrawableAnimated arrow;
    private final Component localizedName;
    private final Vector2i screenPos = new Vector2i();

    public CookingCauldronCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(Meadow.MOD_ID, "textures/gui/cooking_cauldron_gui.png"), 134 - WIDTH_OF, 23 - HEIGHT_OF, 124, 55);
        this.arrow = helper.drawableBuilder(new ResourceLocation(Meadow.MOD_ID, "textures/gui/cooking_cauldron_gui.png"), 176 - WIDTH_OF, 31 - HEIGHT_OF, 17, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ObjectRegistry.COOKING_CAULDRON.get().asItem().getDefaultInstance());
        this.burnicon = helper.createDrawable(new ResourceLocation(Meadow.MOD_ID, "textures/gui/cooking_cauldron_gui.png"), 176 - WIDTH_OF, 0 - HEIGHT_OF, 17, 15);
        this.localizedName = Component.translatable("rei.meadow.cooking_cauldron_category");
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CookingCauldronRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 30 - WIDTH_OF + (i % 3) * 18, 17 - HEIGHT_OF + (i / 3) * 18)
                    .addIngredients(ingredients.get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 124 - WIDTH_OF, 26 - HEIGHT_OF)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(CookingCauldronRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 70 - WIDTH_OF, 27 - HEIGHT_OF);
        burnicon.draw(guiGraphics, 108 - WIDTH_OF, 52 - HEIGHT_OF);
    }

    @Override
    public @NotNull RecipeType<CookingCauldronRecipe> getRecipeType() {
        return COOKING_CAULDRON;
    }

    @Override
    public @NotNull Component getTitle() {
        return this.localizedName;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }
}
