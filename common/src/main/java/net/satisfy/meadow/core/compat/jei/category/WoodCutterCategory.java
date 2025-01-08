package net.satisfy.meadow.core.compat.jei.category;

import mezz.jei.api.constants.ModIds;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.meadow.Meadow;
import net.satisfy.meadow.core.recipes.WoodcuttingRecipe;
import net.satisfy.meadow.core.registry.ObjectRegistry;

public class WoodCutterCategory implements IRecipeCategory<WoodcuttingRecipe> {
    public static final RecipeType<WoodcuttingRecipe> WOODCUTTER = RecipeType.create(Meadow.MOD_ID, "woodcutting", WoodcuttingRecipe.class);
    public static final int width = 82;
    public static final int height = 34;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public WoodCutterCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png");
        background = helper.createDrawable(location, 0, 220, width, height);
        icon = helper.createDrawableItemStack(ObjectRegistry.WOODCUTTER.get().asItem().getDefaultInstance());
        this.localizedName = Component.translatable("rei.meadow.wood_cutter_category");
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WoodcuttingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61,  9)
                .addItemStack(recipe.getResultItem());
    }


    @Override
    public RecipeType<WoodcuttingRecipe> getRecipeType() {
        return WOODCUTTER;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }
}
