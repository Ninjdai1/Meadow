package net.satisfy.meadow.core.compat.rei;

import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.satisfy.meadow.core.compat.rei.category.CheeseFormCategory;
import net.satisfy.meadow.core.compat.rei.category.CookingCauldronCategory;
import net.satisfy.meadow.core.compat.rei.category.FondueCategory;
import net.satisfy.meadow.core.compat.rei.display.CheeseFormDisplay;
import net.satisfy.meadow.core.compat.rei.display.CookingCauldronDisplay;
import net.satisfy.meadow.core.compat.rei.display.FondueDisplay;
import net.satisfy.meadow.core.recipes.CheeseFormRecipe;
import net.satisfy.meadow.core.recipes.CookingCauldronRecipe;
import net.satisfy.meadow.core.recipes.FondueRecipe;
import net.satisfy.meadow.core.registry.ObjectRegistry;

public class MeadowReiClientPlugin {

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new CookingCauldronCategory());
        registry.add(new CheeseFormCategory());
        registry.add(new FondueCategory());

        registry.addWorkstations(CookingCauldronCategory.COOKING_CAULDRON_DISPLAY, EntryStacks.of(ObjectRegistry.COOKING_CAULDRON.get()));
        registry.addWorkstations(CheeseFormCategory.CHEESE_FORM_DISPLAY, EntryStacks.of(ObjectRegistry.CHEESE_FORM.get()));
        registry.addWorkstations(FondueCategory.FONDUE_DISPLAY, EntryStacks.of(ObjectRegistry.FONDUE.get()));
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(CookingCauldronRecipe.class, CookingCauldronDisplay::new);
        registry.registerFiller(CheeseFormRecipe.class, CheeseFormDisplay::new);
        registry.registerFiller(FondueRecipe.class, FondueDisplay::new);
    }
}
