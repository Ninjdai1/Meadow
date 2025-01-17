package net.satisfy.meadow.core.block;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.meadow.core.block.entity.WoodcutterBlockEntity;
import net.satisfy.meadow.core.registry.SoundEventRegistry;
import net.satisfy.meadow.core.registry.TagRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class WoodcutterBlock extends Block implements EntityBlock {
    public static final BooleanProperty HAS_AXE = BooleanProperty.create("has_axe");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    public WoodcutterBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HAS_AXE, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (world.getBlockEntity(pos) instanceof WoodcutterBlockEntity blockEntity) {
            if (player.isShiftKeyDown()) {
                if (!world.isClientSide && blockEntity.hasAxe()) {
                    ItemStack axe = blockEntity.getAxe();
                    if (!player.addItem(axe)) {
                        player.drop(axe, false);
                    }
                    blockEntity.removeAxe();
                    world.setBlock(pos, state.setValue(HAS_AXE, false), 3);
                    return InteractionResult.SUCCESS;
                }
            }

            if (isAxe(heldItem)) {
                if (!world.isClientSide) {
                    if (!blockEntity.hasAxe()) {
                        blockEntity.setAxe(heldItem.copy());
                        if (!player.isCreative()) {
                            heldItem.shrink(1);
                        }
                        world.setBlock(pos, state.setValue(HAS_AXE, true), 3);
                    } else {
                        blockEntity.dropAxe();
                    }
                }
                return InteractionResult.SUCCESS;
            }

            if (isWoodOrLog(heldItem.getItem())) {
                if (!world.isClientSide && blockEntity.hasAxe()) {
                    int output = getOutputAmount(blockEntity.getAxe());
                    Optional<String> woodType = getWoodType(heldItem.getItem());
                    if (woodType.isPresent()) {
                        ResourceLocation itemRL = BuiltInRegistries.ITEM.getKey(heldItem.getItem());
                        String namespace = itemRL.getNamespace();
                        String type = woodType.get();
                        Item planks = getPlanksForWoodType(namespace, type);
                        if (planks != Items.AIR) {
                            spawnPlanks(world, pos, planks, output);
                            if (!player.isCreative()) {
                                heldItem.shrink(1);
                            }
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean isAxe(ItemStack itemStack) {
        return itemStack.is(ItemTags.AXES);
    }

    private int getOutputAmount(ItemStack axe) {
        Item item = axe.getItem();
        if (item == Items.WOODEN_AXE || item == Items.STONE_AXE) {
            return 4;
        } else if (item == Items.IRON_AXE) {
            return 5;
        } else if (item == Items.DIAMOND_AXE) {
            return 6;
        } else if (item == Items.NETHERITE_AXE) {
            return 7;
        }
        return 5;
    }

    private void spawnPlanks(Level world, BlockPos pos, Item planks, int amount) {
        for (int i = 0; i < amount; i++) {
            ItemStack plankStack = new ItemStack(planks);
            Vec3 spawnPos = Vec3.atCenterOf(pos).add(0, 0.5, 0);
            ItemEntity itemEntity = new ItemEntity(world, spawnPos.x, spawnPos.y, spawnPos.z, plankStack);
            itemEntity.setDeltaMovement(0, 0.1, 0);
            world.addFreshEntity(itemEntity);
        }
        playDropSound(world, pos);
    }

    private boolean isWoodOrLog(Item item) {
        TagKey<Item> logsTag = ItemTags.LOGS;
        TagKey<Item> woodTag = TagRegistry.IS_WOODCUTTER_USABLE;

        return item.builtInRegistryHolder().is(logsTag) ||
                item.builtInRegistryHolder().is(woodTag);
    }

    private Optional<String> getWoodType(Item item) {
        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(item);
        String path = registryName.getPath();
        if (path.contains("stripped_")) {
            path = path.replace("stripped_", "");
        }
        if (path.endsWith("_log") || path.endsWith("_wood")) {
            String[] parts = path.split("_");
            if (parts.length >= 2) {
                if (parts.length == 3 && parts[0].equals("dark") && parts[1].equals("oak")) {
                    return Optional.of("dark_oak");
                }
                return Optional.of(parts[0]);
            }
        }
        return Optional.empty();
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof WoodcutterBlockEntity blockEntity) {
                ItemStack axe = blockEntity.getAxe();
                if (!axe.isEmpty()) {
                    Vec3 dropPos = Vec3.atCenterOf(pos);
                    ItemEntity itemEntity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, axe);
                    world.addFreshEntity(itemEntity);
                    blockEntity.removeAxe();
                }
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    private Item getPlanksForWoodType(String namespace, String woodType) {
        ResourceLocation planksRL = new ResourceLocation(namespace, woodType + "_planks");
        return BuiltInRegistries.ITEM.get(planksRL);
    }

    private void playDropSound(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            world.playSound(null, pos, SoundEventRegistry.WOODCUTTER.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_AXE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WoodcutterBlockEntity(blockPos, blockState);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.meadow.description.title").withStyle(style -> style.withColor(TextColor.fromRgb(0x4CAF50)).withBold(true)));
            tooltip.add(Component.translatable("tooltip.meadow.description.woodcutter_1").withStyle(style -> style.withColor(TextColor.fromRgb(0x4CAF50)).withItalic(false)));
            tooltip.add(Component.translatable("tooltip.meadow.description.woodcutter_2").withStyle(style -> style.withColor(TextColor.fromRgb(0x4CAF50)).withItalic(false)));
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltip.meadow.description.woodcutter_3").withStyle(style -> style.withColor(TextColor.fromRgb(0x4CAF50)).withItalic(false)));
        }
    }
}
