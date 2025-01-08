package net.satisfy.meadow.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WindowBlock extends IronBarsBlock {
    public static final IntegerProperty PART = IntegerProperty.create("part", 0, 3);

    public WindowBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(WATERLOGGED, false)
                .setValue(PART, 0));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide()) {
            this.updateWindows(world, pos, state);
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    private void updateWindows(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockPos lowest = this.getLowestWindow(world, pos);
        BlockPos highest = this.getHighestWindow(world, pos);
        int height = 0;

        BlockPos current;
        for(current = lowest; current.getY() <= highest.getY(); current = current.above()) {
            ++height;
        }

        if (height == 1) {
            BlockState newState = state.setValue(PART, 0);
            if (!world.getBlockState(lowest).equals(newState)) {
                world.setBlock(lowest, newState, 3);
            }
        } else if (height == 2) {
            BlockState newStateLow = state.setValue(PART, 1);
            if (!world.getBlockState(lowest).equals(newStateLow)) {
                world.setBlock(lowest, newStateLow, 3);
            }
            BlockState newStateHigh = state.setValue(PART, 3);
            if (!world.getBlockState(highest).equals(newStateHigh)) {
                world.setBlock(highest, newStateHigh, 3);
            }
        } else {
            BlockState newStateLow = state.setValue(PART, 1);
            if (!world.getBlockState(lowest).equals(newStateLow)) {
                world.setBlock(lowest, newStateLow, 3);
            }

            for(current = lowest.above(); current.getY() < highest.getY(); current = current.above()) {
                BlockState newStateMiddle = state.setValue(PART, 2);
                if (!world.getBlockState(current).equals(newStateMiddle)) {
                    world.setBlock(current, newStateMiddle, 3);
                }
            }

            BlockState newStateHigh = state.setValue(PART, 3);
            if (!world.getBlockState(highest).equals(newStateHigh)) {
                world.setBlock(highest, newStateHigh, 3);
            }
        }
    }

    private BlockPos getLowestWindow(LevelAccessor world, BlockPos pos) {
        while(world.getBlockState(pos.below()).getBlock() == this) {
            pos = pos.below();
        }
        return pos;
    }

    private BlockPos getHighestWindow(LevelAccessor world, BlockPos pos) {
        while(world.getBlockState(pos.above()).getBlock() == this) {
            pos = pos.above();
        }
        return pos;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        if (!world.isClientSide()) {
            this.updatePartOnNeighborChange(world, pos, state);
        }
    }

    private void updatePartOnNeighborChange(Level world, BlockPos pos, BlockState state) {
        boolean hasBelow = world.getBlockState(pos.below()).getBlock() == this;
        boolean hasAbove = world.getBlockState(pos.above()).getBlock() == this;
        BlockState newState;
        if (!hasBelow && !hasAbove) {
            newState = state.setValue(PART, 0);
        } else if (!hasBelow) {
            newState = state.setValue(PART, 1);
        } else if (!hasAbove) {
            newState = state.setValue(PART, 3);
        } else {
            newState = state.setValue(PART, 2);
        }
        if (!world.getBlockState(pos).equals(newState)) {
            world.setBlock(pos, newState, 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
}
