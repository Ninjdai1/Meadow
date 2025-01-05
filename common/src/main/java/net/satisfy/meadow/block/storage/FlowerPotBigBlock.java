package net.satisfy.meadow.block.storage;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.meadow.block.StorageBlock;
import net.satisfy.meadow.registry.StorageTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class FlowerPotBigBlock extends StorageBlock {
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.78125F, (double)0.4375F, (double)0.21875F, (double)0.90625F, (double)0.625F, (double)0.78125F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.09375F, (double)0.4375F, (double)0.21875F, (double)0.21875F, (double)0.625F, (double)0.78125F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.21875F, (double)0.0F, (double)0.21875F, (double)0.78125F, (double)0.4375F, (double)0.78125F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.09375F, (double)0.4375F, (double)0.09375F, (double)0.90625F, (double)0.625F, (double)0.21875F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.09375F, (double)0.4375F, (double)0.78125F, (double)0.90625F, (double)0.625F, (double)0.90625F), BooleanOp.OR);
        return shape;
    };
    private static final VoxelShape SHAPE;

    public FlowerPotBigBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    public int size() {
        return 1;
    }

    public ResourceLocation type() {
        return StorageTypeRegistry.FLOWER_POT_BIG;
    }

    public Direction[] unAllowedDirections() {
        return new Direction[0];
    }

    public boolean canInsertStack(ItemStack stack) {
        return stack.is(ItemTags.TALL_FLOWERS);
    }

    public int getSection(Float x, Float y) {
        return 0;
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public SoundEvent getAddSound(Level level, BlockPos blockPos, Player player, int i) {
        return SoundEvents.GRASS_PLACE;
    }

    public SoundEvent getRemoveSound(Level level, BlockPos blockPos, Player player, int i) {
        return SoundEvents.GRASS_BREAK;
    }

    static {
        SHAPE = (VoxelShape)voxelShapeSupplier.get();
    }
}

