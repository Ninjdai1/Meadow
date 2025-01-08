package net.satisfy.meadow.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.satisfy.meadow.core.util.MeadowIdentifier;

import java.util.Set;

public class StorageTypeRegistry {
    public static final ResourceLocation WHEEL_BARROW = new MeadowIdentifier("wheel_barrow");
    public static final ResourceLocation CHEESE_RACK = new MeadowIdentifier("cheese_rack");
    public static final ResourceLocation FLOWER_POT_SMALL = new MeadowIdentifier("flower_pot_small");
    public static final ResourceLocation FLOWER_POT_BIG = new MeadowIdentifier("flower_pot_big");
    public static final ResourceLocation FLOWER_BOX = new MeadowIdentifier("flower_box");

    public static Set<Block> registerBlocks(Set<Block> blocks) {
        blocks.add(ObjectRegistry.WHEELBARROW.get());
        blocks.add(ObjectRegistry.WOODEN_FLOWER_POT_SMALL.get());
        blocks.add(ObjectRegistry.WOODEN_FLOWER_POT_BIG.get());
        blocks.add(ObjectRegistry.WOODEN_FLOWER_BOX.get());
        blocks.add(ObjectRegistry.CHEESE_RACK.get());
        return blocks;
    }
}
