package net.satisfy.meadow.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.meadow.core.util.ShutterType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ShutterBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final EnumProperty<ShutterType> TYPE;
    public static final BooleanProperty LEFT;
    public static final BooleanProperty OPEN;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape[] SHAPES;

    public ShutterBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(((((this.stateDefinition.any().setValue(FACING, Direction.NORTH)).setValue(TYPE, ShutterType.NONE)).setValue(OPEN, false)).setValue(LEFT, false).setValue(POWERED, false)).setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        int shape = state.getValue(FACING).get2DDataValue() + (state.getValue(OPEN) ? (state.getValue(LEFT) ? 3 : 1) : 0);
        return SHAPES[shape % 4];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockState blockState = this.defaultBlockState().setValue(FACING, facing);

        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Vec3 clickLocation = context.getClickLocation();

        boolean left;
        if (facing.getAxis() == Direction.Axis.X) {
            left = clickLocation.z - (double) clickedPos.getZ() > 0.5D;
        } else {
            left = clickLocation.x - (double) clickedPos.getX() > 0.5D;
        }
        if (context.getNearestLookingDirection() == Direction.NORTH || context.getNearestLookingDirection() == Direction.EAST)
            left = !left;
        blockState = blockState.setValue(LEFT, left);

        if (world.hasNeighborSignal(clickedPos)) {
            blockState = blockState.setValue(OPEN, true).setValue(POWERED, true);
        }

        blockState = blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.above()), world.getBlockState(clickedPos.below())));

        return blockState.setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide) return;

        boolean powered = world.hasNeighborSignal(pos);
        if (powered != state.getValue(POWERED)) {
            if (state.getValue(OPEN) != powered) {
                state = state.setValue(OPEN, powered);
                world.playSound(null, pos, shutterSound(powered), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            state = state.setValue(POWERED, powered);
            if (state.getValue(WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
        }
        ShutterType type = getType(state, world.getBlockState(pos.above()), world.getBlockState(pos.below()));
        if (state.getValue(TYPE) != type) {
            state = state.setValue(TYPE, type);
        }
        world.setBlock(pos, state, 3);
    }


    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return toggleShutters(state, level, pos, player);
    }

    public InteractionResult toggleShutters(BlockState state, Level level, BlockPos pos, Player player) {
        state = state.cycle(OPEN);
        level.setBlock(pos, state, 3);
        if (player == null || !player.isCrouching()) toggleShutters(state, level, pos, state.getValue(OPEN));
        level.playSound(null, pos, shutterSound(state.getValue(OPEN)), SoundSource.BLOCKS, 1.0F, 1.0F);

        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void toggleShutters(BlockState state, Level level, BlockPos pos, boolean open) {
        BlockState updateState = state;
        BlockPos updatePos = pos;
        if (state.getValue(TYPE) == ShutterType.MIDDLE || state.getValue(TYPE) == ShutterType.BOTTOM) {
            int heightUp = level.dimensionType().height() - updatePos.getY();
            for (int i = 0; i < heightUp; i++) {
                BlockState above = level.getBlockState(updatePos.above());
                if (above.is(state.getBlock()) && above.getValue(FACING) == updateState.getValue(FACING) && above.getValue(LEFT) == updateState.getValue(LEFT) && above.getValue(OPEN) != open) {
                    updateState = above;
                    updatePos = updatePos.above();
                    level.setBlock(updatePos, updateState.setValue(OPEN, open), 3);
                } else {
                    break;
                }
            }
        }
        if (state.getValue(TYPE) == ShutterType.MIDDLE || state.getValue(TYPE) == ShutterType.TOP) {
            updateState = state;
            updatePos = pos;
            int heightDown = level.dimensionType().minY() - updatePos.getY();
            heightDown = (heightDown < 0) ? -heightDown : heightDown;
            for (int i = 0; i < heightDown; i++) {
                BlockState below = level.getBlockState(updatePos.below());
                if (below.is(state.getBlock()) && below.getValue(FACING) == updateState.getValue(FACING) && below.getValue(LEFT) == updateState.getValue(LEFT) && below.getValue(OPEN) != open) {
                    updateState = below;
                    updatePos = updatePos.below();
                    level.setBlock(updatePos, updateState.setValue(OPEN, open), 3);
                } else {
                    break;
                }
            }
        }
    }

    public static SoundEvent shutterSound(boolean open) {
        if (open) {
            return SoundEvents.BAMBOO_WOOD_DOOR_OPEN;
        }
        return SoundEvents.BAMBOO_WOOD_DOOR_CLOSE;
    }

    public ShutterType getType(BlockState state, BlockState above, BlockState below) {
        boolean shape_above_same = above.getBlock() == state.getBlock() && above.getValue(FACING) == state.getValue(FACING)
                && above.getValue(OPEN) == state.getValue(OPEN) && above.getValue(LEFT) == state.getValue(LEFT);
        boolean shape_below_same = below.getBlock() == state.getBlock() && below.getValue(FACING) == state.getValue(FACING)
                && below.getValue(OPEN) == state.getValue(OPEN) && below.getValue(LEFT) == state.getValue(LEFT);

        if (shape_above_same && !shape_below_same) {
            return ShutterType.BOTTOM;
        } else if (!shape_above_same && shape_below_same) {
            return ShutterType.TOP;
        } else if (shape_above_same) {
            return ShutterType.MIDDLE;
        }
        return ShutterType.NONE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, OPEN, LEFT, POWERED, WATERLOGGED);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        TYPE = ShutterType.SHUTTER_TYPE;
        LEFT = BooleanProperty.create("left");
        OPEN = BlockStateProperties.OPEN;
        POWERED = BlockStateProperties.POWERED;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        SHAPES = new VoxelShape[]{
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D),
                Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D),//north
                Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D)  //east
        };
    }
}
