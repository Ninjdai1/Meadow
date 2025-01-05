package net.satisfy.meadow.block.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.meadow.block.StorageBlock;
import net.satisfy.meadow.registry.StorageTypeRegistry;
import net.satisfy.meadow.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

public class FlowerBoxBlock extends StorageBlock {
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.9375F, (double)0.0F, (double)0.5625F, (double)1.0F, (double)0.375F, (double)1.0F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.0F, (double)0.0F, (double)0.5625F, (double)0.0625F, (double)0.375F, (double)1.0F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.0625F, (double)0.0F, (double)0.5625F, (double)0.9375F, (double)0.375F, (double)0.625F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.0625F, (double)0.0F, (double)0.9375F, (double)0.9375F, (double)0.375F, (double)1.0F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box((double)0.0625F, (double)0.0F, (double)0.625F, (double)0.9375F, (double)0.3125F, (double)0.9375F), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> SHAPE = (Map)Util.make(new HashMap(), (map) -> {
        for(Direction direction : Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, (VoxelShape)voxelShapeSupplier.get()));
        }

    });

    public FlowerBoxBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return (VoxelShape)SHAPE.get(state.getValue(FACING));
    }

    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return player.isShiftKeyDown() ? InteractionResult.PASS : super.use(state, world, pos, player, hand, hit);
    }

    public int size() {
        return 2;
    }

    public ResourceLocation type() {
        return StorageTypeRegistry.FLOWER_BOX;
    }

    public Direction[] unAllowedDirections() {
        return new Direction[]{Direction.DOWN};
    }

    public boolean canInsertStack(ItemStack stack) {
        return stack.is(ItemTags.SMALL_FLOWERS);
    }

    public int getSection(Float x, Float y) {
        return (double)x < (double)0.5F ? 0 : 1;
    }
}

