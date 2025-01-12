package net.satisfy.meadow.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.meadow.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

public class DoormatBlock extends LineConnectingBlock {
    private static final VoxelShape SHAPE_MIDDLE = Block.box(0, 0, 3, 16, 1, 13); 
    private static final VoxelShape SHAPE_SINGLE = Block.box(1, 0, 3, 15, 1, 13); 
    private static final VoxelShape SHAPE_RIGHT = Block.box(0, 0, 3, 15, 1, 13);
    private static final VoxelShape SHAPE_LEFT = Block.box(1, 0, 3, 16, 1, 13);  // 15x1x10 (rechts)

    public DoormatBlock(Properties settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        GeneralUtil.LineConnectingType type = state.getValue(TYPE);
        return switch (type) {
            case MIDDLE -> SHAPE_MIDDLE;
            case LEFT -> SHAPE_LEFT;
            case RIGHT -> SHAPE_RIGHT;
            case NONE -> SHAPE_SINGLE;
        };
    }
}
