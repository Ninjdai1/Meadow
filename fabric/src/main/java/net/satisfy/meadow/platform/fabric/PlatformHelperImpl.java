package net.satisfy.meadow.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.satisfy.meadow.Meadow;
import net.satisfy.meadow.fabric.core.config.MeadowFabricConfig;
import net.satisfy.meadow.platform.PlatformHelper;

import java.util.function.Supplier;

public class PlatformHelperImpl extends PlatformHelper {
    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        EntityType<T> registry = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Meadow.MOD_ID, name), FabricEntityTypeBuilder.create(category, factory).dimensions(EntityDimensions.scalable(width, height)).trackRangeChunks(clientTrackingRange).build());
        return () -> registry;
    }

    public static boolean shouldGiveEffect() {
        MeadowFabricConfig config = AutoConfig.getConfigHolder(MeadowFabricConfig.class).getConfig();
        return config.items.banner.giveEffect;
    }

    public static boolean shouldShowTooltip() {
        MeadowFabricConfig config = AutoConfig.getConfigHolder(MeadowFabricConfig.class).getConfig();
        return config.items.banner.isShowTooltipEnabled();
    }
}
