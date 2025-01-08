package net.satisfy.meadow.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.satisfy.meadow.core.block.entity.FondueBlockEntity;
import net.satisfy.meadow.core.registry.ScreenHandlerRegistry;
import org.jetbrains.annotations.NotNull;

public class FondueGuiHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;

    public FondueGuiHandler(int syncId, Inventory inventory) {
        this(syncId, inventory, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public FondueGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData delegate) {
        super(ScreenHandlerRegistry.FONDUE_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(inventory, 3);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = delegate;


        buildBlockEntityContainer(playerInventory, inventory);
        buildPlayerContainer(playerInventory);

        addDataSlots(delegate);
    }

    private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
        this.addSlot(new Slot(inventory, 0, 41, 9));
        this.addSlot(new Slot(inventory, 1, 41, 33)); //Fuel
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 2, 120, 25));
    }

    private void buildPlayerContainer(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public boolean getIsCooking() {
        return propertyDelegate.get(0) != 0;
    }


    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int arrowWidth = 15;

        if (progress == 0) {
            return 0;
        }
        return progress * (arrowWidth - 1) / FondueBlockEntity.MAX_PROGRESS + 1;
    }
}