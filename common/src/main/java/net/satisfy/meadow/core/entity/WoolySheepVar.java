package net.satisfy.meadow.core.entity;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.satisfy.meadow.core.registry.TagRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public enum WoolySheepVar implements StringRepresentable {
    FLECKED(0, "flecked_sheep", new float[]{1.0F, 1.0F, 1.0F}),
    PATCHED(1, "patched_sheep", new float[]{0.8F, 0.8F, 0.8F}),
    ROCKY(2, "rocky_sheep", new float[]{0.6F, 0.6F, 0.6F}),
    INKY(3, "inky_sheep", new float[]{0.2F, 0.2F, 0.2F}),
    FUZZY(4, "fuzzy_sheep", new float[]{0.9F, 0.7F, 0.5F}),
    LONG_NOSED(5, "long_nosed_sheep", new float[]{0.7F, 0.5F, 0.3F});

    public static final Codec<WoolySheepVar> CODEC = StringRepresentable.fromEnum(WoolySheepVar::values);
    private static final IntFunction<WoolySheepVar> BY_ID = ByIdMap.continuous(WoolySheepVar::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String name;
    private final float[] textureDiffuseColors;

    WoolySheepVar(int id, String name, float[] textureDiffuseColors) {
        this.id = id;
        this.name = name;
        this.textureDiffuseColors = textureDiffuseColors;
    }

    public int getId() {
        return this.id;
    }

    public static WoolySheepVar byId(int i) {
        return BY_ID.apply(i);
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    private static final Map<WoolySheepVar, TagKey<Biome>> SPAWNS = Util.make(new HashMap<>(), map -> {
        map.put(FLECKED, TagRegistry.IS_MEADOW);
        map.put(PATCHED, BiomeTags.IS_FOREST);
        map.put(ROCKY, TagRegistry.SPAWNS_ROCKY_SHEEP);
        map.put(INKY, BiomeTags.IS_SAVANNA);
        map.put(FUZZY, BiomeTags.IS_FOREST);
        map.put(LONG_NOSED, BiomeTags.IS_MOUNTAIN);
    });

    public static WoolySheepVar getRandomVariant(LevelAccessor levelAccessor, BlockPos blockPos, boolean spawnEgg) {
        Holder<Biome> holder = levelAccessor.getBiome(blockPos);
        RandomSource random = levelAccessor.getRandom();
        List<WoolySheepVar> possibleVars = getShearableSheepVariantsInBiome(holder);
        int size = possibleVars.size();
        if(size == 0 || spawnEgg){
            if(spawnEgg) return Util.getRandom(WoolySheepVar.values(), random);
            if(holder.is(TagRegistry.IS_MEADOW)) return FLECKED;
            List<WoolySheepVar> list = new java.util.ArrayList<>(List.of(WoolySheepVar.values()));
            return Util.getRandom(list, random);
        }
        return possibleVars.get(random.nextInt(size));
    }

    private static List<WoolySheepVar> getShearableSheepVariantsInBiome(Holder<Biome> biome) {
        return SPAWNS.keySet().stream()
                .filter(variant -> biome.is(SPAWNS.get(variant)))
                .collect(Collectors.toList());
    }

    public float[] getTextureDiffuseColors() {
        return this.textureDiffuseColors;
    }
}
