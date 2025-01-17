package net.satisfy.meadow.core.block.entity;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.meadow.client.gui.handler.FondueGuiHandler;
import net.satisfy.meadow.core.recipes.FondueRecipe;
import net.satisfy.meadow.core.registry.EntityTypeRegistry;
import net.satisfy.meadow.core.registry.RecipeRegistry;
import net.satisfy.meadow.core.world.ImplementedInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FondueBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory, BlockEntityTicker<FondueBlockEntity> {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    private static final int[] SLOTS_FOR_SIDE = new int[]{1};
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2};
    protected final ContainerData propertyDelegate;
    private int progress = 0;
    public static final int MAX_PROGRESS = 72;

    private int fuelAmount = 0;

    public FondueBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.FONDUE.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                if (index == 0) {
                    return FondueBlockEntity.this.progress;
                }
                return 0;
            }

            public void set(int index, int value) {
                if (index == 0) {
                    FondueBlockEntity.this.progress = value;
                }
            }

            public int getCount() {
                return 1;
            }
        };
    }


    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        if (side.equals(Direction.UP)) {
            return SLOTS_FOR_UP;
        } else if (side.equals(Direction.DOWN)) {
            return SLOTS_FOR_DOWN;
        } else return SLOTS_FOR_SIDE;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.nullToEmpty("");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new FondueGuiHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("fondue.progress", progress);
        nbt.putInt("fondue.fuelAmount", fuelAmount);
    }

    @Override
    public void load(CompoundTag nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getInt("fondue.progress");
        fuelAmount = nbt.getInt("fondue.fuelAmount");
        super.load(nbt);

    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    public void tick(Level world, BlockPos blockPos, BlockState state, FondueBlockEntity entity) {
        if (world.isClientSide()) return;

        Recipe<?> r = world.getRecipeManager().getRecipeFor(RecipeRegistry.FONDUE.get(), this, world).orElse(null);
        if (!(r instanceof FondueRecipe recipe)) {
            entity.resetProgress();
            setChanged(world, blockPos, state);
            return;
        }

        if (hasFuel(entity, recipe) && hasRecipe(entity, recipe)) {
            entity.progress++;
            setChanged(world, blockPos, state);
            if (entity.progress >= MAX_PROGRESS) {
                craftItem(entity, recipe);
            }
        } else {
            entity.resetProgress();
            setChanged(world, blockPos, state);
        }
    }

    private static boolean hasFuel(FondueBlockEntity entity, FondueRecipe recipe) {
        if (entity.fuelAmount > 0) return true;
        ItemStack stack = entity.getItem(1);
        if (recipe.getFuel().test(stack)) {
            entity.fuelAmount = 10;
            stack.shrink(1);
            return true;
        }
        return false;
    }

    private static void craftItem(FondueBlockEntity entity, FondueRecipe recipe) {
        entity.removeItem(0, 1);

        ItemStack stack = recipe.assemble();
        stack.setCount(entity.getItem(2).getCount() + 1);
        entity.setItem(2, stack);

        entity.resetProgress();
        entity.fuelAmount--;
    }

    private static boolean hasRecipe(FondueBlockEntity entity, FondueRecipe recipe) {
        ItemStack result = recipe.getResultItem();

        boolean hasBreadInFirstSlot = recipe.getBread().test(entity.getItem(0));

        boolean r2 = canInsertAmountIntoOutputSlot(entity.inventory, result.getCount());
        boolean r3 = canInsertItemIntoOutputSlot(entity.inventory, result.getItem());

        return hasBreadInFirstSlot && r2 && r3;
    }

    @Override
    public boolean stillValid(Player player) {
        assert this.level != null;
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5, (double) this.worldPosition.getY() + 0.5, (double) this.worldPosition.getZ() + 0.5) <= 64.0;
        }
    }

    private static boolean canInsertItemIntoOutputSlot(NonNullList<ItemStack> inventory, Item output) {
        return inventory.get(2).getItem() == output || inventory.get(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(NonNullList<ItemStack> inventory, int additionalAmount) {
        return inventory.get(2).getMaxStackSize() >= inventory.get(2).getCount() + additionalAmount;
    }
}
