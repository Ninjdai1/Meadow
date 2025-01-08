package net.satisfy.meadow.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.satisfy.meadow.client.MeadowClient;
import net.satisfy.meadow.fabric.client.renderer.FurBootsRenderer;
import net.satisfy.meadow.fabric.client.renderer.FurChestplateRenderer;
import net.satisfy.meadow.fabric.client.renderer.FurHelmetRenderer;
import net.satisfy.meadow.fabric.client.renderer.FurLeggingsRenderer;
import net.satisfy.meadow.core.registry.ObjectRegistry;

public class MeadowFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MeadowClient.preInitClient();
        MeadowClient.initClient();

        ArmorRenderer.register(new FurHelmetRenderer(), ObjectRegistry.FUR_HELMET.get());
        ArmorRenderer.register(new FurChestplateRenderer(), ObjectRegistry.FUR_CHESTPLATE.get());
        ArmorRenderer.register(new FurLeggingsRenderer(), ObjectRegistry.FUR_LEGGINGS.get());
        ArmorRenderer.register(new FurBootsRenderer(), ObjectRegistry.FUR_BOOTS.get());
    }
}
