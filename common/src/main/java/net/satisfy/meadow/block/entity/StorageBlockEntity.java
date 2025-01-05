package net.satisfy.meadow.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.meadow.registry.BlockEntityRegistry;
import net.satisfy.meadow.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class StorageBlockEntity extends BlockEntity {
    private int size;
    private NonNullList<ItemStack> inventory;

    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType) BlockEntityRegistry.STORAGE_ENTITY.get(), pos, state);
    }

    public StorageBlockEntity(BlockPos pos, BlockState state, int size) {
        super((BlockEntityType)BlockEntityRegistry.STORAGE_ENTITY.get(), pos, state);
        this.size = size;
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
    }

    public void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
            this.setChanged();
        }

    }

    public ItemStack removeStack(int slot) {
        if (slot >= 0 && slot < this.inventory.size()) {
            ItemStack stack = (ItemStack)this.inventory.set(slot, ItemStack.EMPTY);
            this.setChanged();
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void setChanged() {
        Level var2 = this.level;
        if (var2 instanceof ServerLevel serverLevel) {
            if (!this.level.isClientSide()) {
                Packet<ClientGamePacketListener> updatePacket = this.getUpdatePacket();
                Iterator var3 = Util.tracking(serverLevel, this.getBlockPos()).iterator();

                while(var3.hasNext()) {
                    ServerPlayer player = (ServerPlayer)var3.next();
                    player.connection.send(updatePacket);
                }
            }
        }

        super.setChanged();
    }

    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.size = nbt.getInt("size");
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory);
    }

    protected void saveAdditional(CompoundTag nbt) {
        ContainerHelper.saveAllItems(nbt, this.inventory);
        nbt.putInt("size", this.size);
        super.saveAdditional(nbt);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        for(int i = 0; i < inventory.size(); ++i) {
            this.inventory.set(i, (ItemStack)inventory.get(i));
        }

    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }
}

