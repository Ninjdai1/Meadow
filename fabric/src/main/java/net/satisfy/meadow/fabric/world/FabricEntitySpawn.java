package net.satisfy.meadow.fabric.world;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.satisfy.meadow.core.registry.EntityTypeRegistry;

public class FabricEntitySpawn {
    public static void registerEntitySpawn() {
        SpawnPlacements.register(EntityTypeRegistry.WATER_BUFFALO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);

        SpawnPlacements.register(EntityTypeRegistry.SHEARABLE_MEADOW_VAR_COW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    }
}
