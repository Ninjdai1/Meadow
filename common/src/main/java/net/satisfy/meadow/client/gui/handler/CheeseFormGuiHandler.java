package net.satisfy.meadow.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.satisfy.meadow.core.block.entity.CheeseFormBlockEntity;
import net.satisfy.meadow.core.registry.ScreenHandlerRegistry;
import org.jetbrains.annotations.NotNull;

public class CheeseFormGuiHandler extends AbstractContainerMenu {

    private final ContainerData propertyDelegate;

    public CheeseFormGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(3), new SimpleContainerData(1));
    }

    public CheeseFormGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerRegistry.CHEESE_FORM_SCREEN_HANDLER.get(), syncId);
        this.propertyDelegate = propertyDelegate;
        addDataSlots(propertyDelegate);
        buildBlockEntityContainer(playerInventory, inventory);
        buildPlayerContainer(playerInventory);
    }

    private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 0, 123, 34));
        this.addSlot(new Slot(inventory, 1, 33, 33));
        this.addSlot(new Slot(inventory, 2, 51, 33));
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

    public int getScaledXProgress() {
        final int progress = this.propertyDelegate.get(0);
        final int totalProgress = CheeseFormBlockEntity.COOKING_TIME_IN_TICKS;
        if (progress == 0) {
            return 0;
        }
        return progress * 24 / totalProgress + 1;
    }

    public int getScaledYProgress() {
        final int progress = this.propertyDelegate.get(0);
        final int totalProgress = CheeseFormBlockEntity.COOKING_TIME_IN_TICKS;
        if (progress == 0) {
            return 0;
        }
        return progress * 25 / totalProgress + 1;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY; // Return an empty stack for now
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // Adjust the logic for still-valid checks
    }
}
