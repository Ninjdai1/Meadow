package net.satisfy.meadow.core.util;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.meadow.Meadow;

public class MeadowIdentifier extends ResourceLocation {
    public MeadowIdentifier(String path) {
        super(Meadow.MOD_ID, path);
    }
}

