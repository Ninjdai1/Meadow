package net.satisfy.meadow.platform.forge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.satisfy.meadow.Meadow;
import net.satisfy.meadow.forge.core.config.MeadowForgeConfig;

import java.util.function.Supplier;

public class PlatformHelperImpl {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Meadow.MOD_ID);

    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, category).sized(width, height).build(name));
    }

    public static boolean shouldGiveEffect() {
        return MeadowForgeConfig.GIVE_EFFECT.get();
    }

    public static boolean shouldShowTooltip() {
        return MeadowForgeConfig.GIVE_EFFECT.get() && MeadowForgeConfig.SHOW_TOOLTIP.get();
    }
}
