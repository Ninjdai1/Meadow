package net.satisfy.meadow.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.meadow.Meadow;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Meadow.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> MEADOW_TAB = CREATIVE_MODE_TABS.register("meadow", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.PIECE_OF_CHEESE.get()))
            .title(Component.translatable("creativetab.meadow.tab"))
            .displayItems((parameters, out) -> {
                out.accept(ObjectRegistry.ALPINE_SALT_ORE.get());
                out.accept(ObjectRegistry.ALPINE_COAL_ORE.get());
                out.accept(ObjectRegistry.ALPINE_LAPIS_ORE.get());
                out.accept(ObjectRegistry.ALPINE_GOLD_ORE.get());
                out.accept(ObjectRegistry.ALPINE_EMERALD_ORE.get());
                out.accept(ObjectRegistry.ALPINE_IRON_ORE.get());
                out.accept(ObjectRegistry.ALPINE_COPPER_ORE.get());
                out.accept(ObjectRegistry.ALPINE_DIAMOND_ORE.get());
                out.accept(ObjectRegistry.ALPINE_REDSTONE_ORE.get());
                out.accept(ObjectRegistry.LIMESTONE.get());
                out.accept(ObjectRegistry.LIMESTONE_STAIRS.get());
                out.accept(ObjectRegistry.LIMESTONE_SLAB.get());
                out.accept(ObjectRegistry.COBBLED_LIMESTONE.get());
                out.accept(ObjectRegistry.COBBLED_LIMESTONE_STAIRS.get());
                out.accept(ObjectRegistry.COBBLED_LIMESTONE_SLAB.get());
                out.accept(ObjectRegistry.LIMESTONE_BRICKS.get());
                out.accept(ObjectRegistry.LIMESTONE_BRICK_STAIRS.get());
                out.accept(ObjectRegistry.LIMESTONE_BRICK_SLAB.get());
                out.accept(ObjectRegistry.MOSSY_COBBLED_LIMESTONE.get());
                out.accept(ObjectRegistry.MOSSY_COBBLED_LIMESTONE_STAIRS.get());
                out.accept(ObjectRegistry.MOSSY_COBBLED_LIMESTONE_SLAB.get());
                out.accept(ObjectRegistry.MOSSY_LIMESTONE_BRICKS.get());
                out.accept(ObjectRegistry.MOSSY_LIMESTONE_BRICK_STAIRS.get());
                out.accept(ObjectRegistry.MOSSY_LIMESTONE_BRICK_SLAB.get());
                out.accept(ObjectRegistry.CRACKED_LIMESTONE_BRICKS.get());
                out.accept(ObjectRegistry.CHISELED_LIMESTONE_BRICKS.get());
                out.accept(ObjectRegistry.POLISHED_LIMESTONE_BRICKS.get());
                out.accept(ObjectRegistry.LIMESTONE_WALL.get());
                out.accept(ObjectRegistry.COBBLED_LIMESTONE_WALL.get());
                out.accept(ObjectRegistry.LIMESTONE_BRICK_WALL.get());
                out.accept(ObjectRegistry.MOSSY_COBBLED_LIMESTONE_WALL.get());
                out.accept(ObjectRegistry.MOSSY_LIMESTONE_BRICK_WALL.get());
                out.accept(ObjectRegistry.STOVE.get());
                out.accept(ObjectRegistry.STOVE_WOOD.get());
                out.accept(ObjectRegistry.STOVE_LID.get());
                out.accept(ObjectRegistry.STOVE_BENCH.get());
                out.accept(ObjectRegistry.PINE_LOG.get());
                out.accept(ObjectRegistry.PINE_WOOD.get());
                out.accept(ObjectRegistry.STRIPPED_PINE_WOOD.get());
                out.accept(ObjectRegistry.STRIPPED_PINE_LOG.get());
                out.accept(ObjectRegistry.PINE_BEAM.get());
                out.accept(ObjectRegistry.PINE_PLANKS.get());
                out.accept(ObjectRegistry.PINE_STAIRS.get());
                out.accept(ObjectRegistry.PINE_SLAB.get());
                out.accept(ObjectRegistry.PINE_PRESSURE_PLATE.get());
                out.accept(ObjectRegistry.PINE_TRAPDOOR.get());
                out.accept(ObjectRegistry.PINE_DOOR.get());
                out.accept(ObjectRegistry.PINE_BARN_TRAPDOOR.get());
                out.accept(ObjectRegistry.PINE_BARN_DOOR.get());
                out.accept(ObjectRegistry.PINE_FENCE.get());
                out.accept(ObjectRegistry.PINE_FENCE_GATE.get());
                out.accept(ObjectRegistry.PINE_RAILING.get());
                out.accept(ObjectRegistry.SHUTTER_BLOCK.get());
                out.accept(ObjectRegistry.SHELF.get());
                out.accept(ObjectRegistry.CHEESE_RACK.get());
                out.accept(ObjectRegistry.CHAIR.get());
                out.accept(ObjectRegistry.TABLE.get());
                out.accept(ObjectRegistry.BENCH.get());
                out.accept(ObjectRegistry.STONE_TABLE.get());
                out.accept(ObjectRegistry.STONE_BENCH.get());
                out.accept(ObjectRegistry.HEART_PATTERNED_WINDOW.get());
                out.accept(ObjectRegistry.SUN_PATTERNED_WINDOW.get());
                out.accept(ObjectRegistry.PINE_WINDOW.get());
                out.accept(ObjectRegistry.FLECKED_WOOL.get());
                out.accept(ObjectRegistry.FLECKED_CARPET.get());
                out.accept(ObjectRegistry.FLECKED_BED.get());
                out.accept(ObjectRegistry.HIGHLAND_WOOL.get());
                out.accept(ObjectRegistry.HIGHLAND_CARPET.get());
                out.accept(ObjectRegistry.HIGHLAND_BED.get());
                out.accept(ObjectRegistry.PATCHED_WOOL.get());
                out.accept(ObjectRegistry.PATCHED_CARPET.get());
                out.accept(ObjectRegistry.PATCHED_BED.get());
                out.accept(ObjectRegistry.ROCKY_WOOL.get());
                out.accept(ObjectRegistry.ROCKY_CARPET.get());
                out.accept(ObjectRegistry.ROCKY_BED.get());
                out.accept(ObjectRegistry.UMBRA_WOOL.get());
                out.accept(ObjectRegistry.UMBRA_CARPET.get());
                out.accept(ObjectRegistry.UMBRA_BED.get());
                out.accept(ObjectRegistry.INKY_WOOL.get());
                out.accept(ObjectRegistry.INKY_CARPET.get());
                out.accept(ObjectRegistry.INKY_BED.get());
                out.accept(ObjectRegistry.WARPED_WOOL.get());
                out.accept(ObjectRegistry.WARPED_CARPET.get());
                out.accept(ObjectRegistry.WARPED_BED.get());
                out.accept(ObjectRegistry.STRAW_BED.get());
                out.accept(ObjectRegistry.CHEESE_FORM.get());
                out.accept(ObjectRegistry.FONDUE.get());
                out.accept(ObjectRegistry.COOKING_CAULDRON.get());
                out.accept(ObjectRegistry.FRAME.get());
                out.accept(ObjectRegistry.WOODCUTTER.get());
                out.accept(ObjectRegistry.WOODEN_CAULDRON.get());
                out.accept(ObjectRegistry.WHEELBARROW.get());
                out.accept(ObjectRegistry.FIRE_LOG.get());
                out.accept(ObjectRegistry.CAN.get());
                out.accept(ObjectRegistry.WATERING_CAN.get());
                out.accept(ObjectRegistry.CLIMBING_ROPE_TOPMOUNT.get());
                out.accept(ObjectRegistry.OIL_LANTERN.get());
                out.accept(ObjectRegistry.CAMERA.get());
                out.accept(ObjectRegistry.DOORMAT.get());
                out.accept(ObjectRegistry.WOODEN_FLOWER_POT_BIG.get());
                out.accept(ObjectRegistry.WOODEN_FLOWER_POT_SMALL.get());
                out.accept(ObjectRegistry.WOODEN_FLOWER_BOX.get());
                out.accept(ObjectRegistry.FUR_HELMET.get());
                out.accept(ObjectRegistry.FUR_CHESTPLATE.get());
                out.accept(ObjectRegistry.FUR_LEGGINGS.get());
                out.accept(ObjectRegistry.FUR_BOOTS.get());
                out.accept(ObjectRegistry.SMALL_FIR.get());
                out.accept(ObjectRegistry.PINE_SAPLING.get());
                out.accept(ObjectRegistry.ALPINE_POPPY.get());
                out.accept(ObjectRegistry.DELPHINIUM.get());
                out.accept(ObjectRegistry.SAXIFRAGE.get());
                out.accept(ObjectRegistry.ENZIAN.get());
                out.accept(ObjectRegistry.FIRE_LILY.get());
                out.accept(ObjectRegistry.ERIOPHORUM.get());
                out.accept(ObjectRegistry.ERIOPHORUM_TALL.get());
                out.accept(ObjectRegistry.PINE_LEAVES.get());
                out.accept(ObjectRegistry.ALPINE_BIRCH_LEAVES_HANGING.get());
                out.accept(ObjectRegistry.CHEESECAKE_SLICE.get());
                out.accept(ObjectRegistry.CHEESE_TART_SLICE.get());
                out.accept(ObjectRegistry.PIECE_OF_CHEESE.get());
                out.accept(ObjectRegistry.PIECE_OF_SHEEP_CHEESE.get());
                out.accept(ObjectRegistry.PIECE_OF_GRAIN_CHEESE.get());
                out.accept(ObjectRegistry.PIECE_OF_AMETHYST_CHEESE.get());
                out.accept(ObjectRegistry.PIECE_OF_BUFFALO_CHEESE.get());
                out.accept(ObjectRegistry.PIECE_OF_GOAT_CHEESE.get());
                out.accept(ObjectRegistry.PIECE_OF_WARPED_CHEESE.get());
                out.accept(ObjectRegistry.CHEESECAKE.get());
                out.accept(ObjectRegistry.CHEESE_TART.get());
                out.accept(ObjectRegistry.CHEESE_BLOCK.get());
                out.accept(ObjectRegistry.SHEEP_CHEESE_BLOCK.get());
                out.accept(ObjectRegistry.GRAIN_CHEESE_BLOCK.get());
                out.accept(ObjectRegistry.AMETHYST_CHEESE_BLOCK.get());
                out.accept(ObjectRegistry.BUFFALO_CHEESE_BLOCK.get());
                out.accept(ObjectRegistry.GOAT_CHEESE_BLOCK.get());
                out.accept(ObjectRegistry.WARPED_CHEESE_BLOCK.get());
                out.accept(ObjectRegistry.ALPINE_SALT.get());
                out.accept(ObjectRegistry.RENNET.get());
                out.accept(ObjectRegistry.CHEESE_SANDWICH.get());
                out.accept(ObjectRegistry.CHEESE_ROLL.get());
                out.accept(ObjectRegistry.CHEESE_STICK.get());
                out.accept(ObjectRegistry.RAW_BUFFALO_MEAT.get());
                out.accept(ObjectRegistry.COOKED_BUFFALO_MEAT.get());
                out.accept(ObjectRegistry.ROASTED_HAM.get());
                out.accept(ObjectRegistry.SAUSAGE_WITH_CHEESE.get());
                out.accept(ObjectRegistry.WOODEN_BUCKET.get());
                out.accept(ObjectRegistry.WOODEN_WATER_BUCKET.get());
                out.accept(ObjectRegistry.WOODEN_MILK_BUCKET.get());
                out.accept(ObjectRegistry.WOODEN_SHEEP_MILK_BUCKET.get());
                out.accept(ObjectRegistry.WOODEN_BUFFALO_MILK_BUCKET.get());
                out.accept(ObjectRegistry.WOODEN_GOAT_MILK_BUCKET.get());
                out.accept(ObjectRegistry.WOODEN_WARPED_MILK_BUCKET.get());
                out.accept(ObjectRegistry.WOODEN_GRAIN_MILK_BUCKET.get());
                out.accept(ObjectRegistry.WOODEN_AMETHYST_MILK_BUCKET.get());
                out.accept(ObjectRegistry.WATER_BUFFALO_SPAWN_EGG_ITEM.get());
                out.accept(ObjectRegistry.WOOLY_COW_SPAWN_EGG_ITEM.get());
                out.accept(ObjectRegistry.MEADOW_SHEEP_SPAWN_EGG_ITEM.get());
                out.accept(BoatsAndSignsRegistry.PINE_SIGN.get());
                out.accept(BoatsAndSignsRegistry.PINE_HANGING_SIGN.get());
                out.accept(BoatsAndSignsRegistry.PINE_BOAT.get());
                out.accept(BoatsAndSignsRegistry.PINE_CHEST_BOAT.get());
                out.accept(ObjectRegistry.PINE_BUTTON.get());
                out.accept(ObjectRegistry.MEADOW_STANDARD.get());
            })
            .build());

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}