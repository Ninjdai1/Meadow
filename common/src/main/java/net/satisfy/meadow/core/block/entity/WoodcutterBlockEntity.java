package net.satisfy.meadow.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.meadow.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WoodcutterBlockEntity extends BlockEntity implements Clearable {
    private ItemStack axe = ItemStack.EMPTY;

    public WoodcutterBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.WOOD_CUTTER.get(), pos, state);
    }

    public ItemStack getAxe() {
        return this.axe;
    }

    public void setAxe(ItemStack axe) {
        this.axe = axe;
        this.markUpdated();
    }

    public boolean hasAxe() {
        return !this.axe.isEmpty();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.axe = ItemStack.of(tag.getCompound("Axe"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Axe", this.axe.save(new CompoundTag()));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Axe", this.axe.save(new CompoundTag()));
        return compoundTag;
    }

    public void dropAxe() {
        if (this.hasAxe()) {
            assert this.level != null;
            this.level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), this.axe));
            this.axe = ItemStack.EMPTY;
            this.markUpdated();
        }
    }

    private void markUpdated() {
        this.setChanged();
        Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void clearContent() {
        this.axe = ItemStack.EMPTY;
    }
}
