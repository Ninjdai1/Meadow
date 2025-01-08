package net.satisfy.meadow.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.meadow.core.block.entity.CookingCauldronBlockEntity;
import net.satisfy.meadow.core.recipes.CookingCauldronRecipe;
import net.satisfy.meadow.core.registry.RecipeRegistry;
import net.satisfy.meadow.core.registry.ScreenHandlerRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

public class CookingCauldronGuiHandler extends AbstractContainerMenu {

    private final ContainerData propertyDelegate;

    public CookingCauldronGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(7), new SimpleContainerData(2));
    }

    public CookingCauldronGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerRegistry.COOKING_CAULDRON_SCREEN_HANDLER.get(), syncId);
        this.propertyDelegate = propertyDelegate;
        addDataSlots(propertyDelegate);
        buildBlockEntityContainer(playerInventory, inventory);
        buildPlayerContainer(playerInventory);
    }

    private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 0, 124, 26));
        for (int row = 0; row < 2; row++) {
            for (int slot = 0; slot < 3; slot++) {
                this.addSlot(new Slot(inventory, 1 + slot + (row * 3), 30 + (slot * 18), 17 + (row * 18)));
            }
        }
    }

    private void buildPlayerContainer(Inventory playerInventory) {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public boolean isBeingBurned() {
        return propertyDelegate.get(1) != 0;
    }

    @SuppressWarnings("unused")
    private boolean isItemIngredient(ItemStack stack) {
        return recipeStream().anyMatch(cookingPotRecipe ->
                cookingPotRecipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack)));
    }

    private Stream<CookingCauldronRecipe> recipeStream() {
        return this.getRecipeManager().getAllRecipesFor(RecipeRegistry.COOKING.get()).stream();
    }

    private RecipeManager getRecipeManager() {
        if (this.slots.get(0).container instanceof BlockEntity blockEntity) {
            return Objects.requireNonNull(blockEntity.getLevel()).getRecipeManager();
        }
        throw new IllegalStateException("Unable to get RecipeManager, container is not associated with a BlockEntity.");
    }

    public int getScaledProgress(int arrowWidth) {
        final int progress = this.propertyDelegate.get(0);
        final int totalProgress = CookingCauldronBlockEntity.getMaxCookingTime();
        if (progress == 0) {
            return 0;
        }
        return progress * arrowWidth / totalProgress + 1;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
