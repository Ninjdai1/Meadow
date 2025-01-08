package net.satisfy.meadow.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.meadow.Meadow;
import net.satisfy.meadow.client.gui.CheeseFormGui;
import net.satisfy.meadow.client.gui.CookingCauldronGui;
import net.satisfy.meadow.client.gui.FondueGui;
import net.satisfy.meadow.client.gui.WoodcutterGui;
import net.satisfy.meadow.client.model.*;
import net.satisfy.meadow.client.renderer.block.storage.CheeseRackRenderer;
import net.satisfy.meadow.client.renderer.block.storage.StorageBlockEntityRenderer;
import net.satisfy.meadow.client.renderer.block.storage.WheelBarrowRenderer;
import net.satisfy.meadow.client.renderer.block.storage.FlowerPotSmallRenderer;
import net.satisfy.meadow.client.renderer.entity.PineBoatRenderer;
import net.satisfy.meadow.client.renderer.entity.ShearableVarCowRenderer;
import net.satisfy.meadow.client.renderer.entity.WaterBuffaloRenderer;
import net.satisfy.meadow.core.registry.EntityTypeRegistry;
import net.satisfy.meadow.core.registry.ScreenHandlerRegistry;
import net.satisfy.meadow.core.registry.StorageTypeRegistry;

import static net.satisfy.meadow.core.registry.ObjectRegistry.*;

public class MeadowClient {
    public static final ModelLayerLocation SHEARABLE_MEADOW_COW_MODEL_LAYER = new ModelLayerLocation(new ResourceLocation(Meadow.MOD_ID, "shearable_meadow_cow"), "head");
    public static final ModelLayerLocation WATER_BUFFALO_MODEL_LAYER = new ModelLayerLocation(new ResourceLocation(Meadow.MOD_ID, "water_buffalo"), "head");

    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayers();

    }

    public static void initClient() {


        RenderTypeRegistry.register(RenderType.cutout(),
                PINE_DOOR.get(), PINE_TRAPDOOR.get(), DELPHINIUM.get(), ALPINE_POPPY.get(), SAXIFRAGE.get(),
                ENZIAN.get(), COOKING_CAULDRON.get(), FRAME.get(), TABLE.get(), FIRE_LOG.get(), ERIOPHORUM.get(),
                ERIOPHORUM_TALL.get(), SMALL_FIR.get(), PINE_SAPLING.get(), CHAIR.get(),
                POTTED_DELPHINIUM.get(), POTTED_ALPINE_POPPY.get(), POTTED_SAXIFRAGE.get(), POTTED_ENZIAN.get(),
                POTTED_ERIOPHORUM.get(), ERIOPHORUM_TALL.get(), PINE_SAPLING.get(), POTTED_PINE_SAPLING.get(), FIRE_LILY.get(),
                POTTED_FIRE_LILY.get(), WOODEN_FLOWER_POT_SMALL.get(), FONDUE.get(), OIL_LANTERN.get(), WHEELBARROW.get(),
                PINE_LEAVES_2.get(), WOODEN_FLOWER_POT_BIG.get(), WOODCUTTER.get());

        RenderTypeRegistry.register(RenderType.translucent(), HEART_PATTERNED_WINDOW.get(), SUN_PATTERNED_WINDOW.get(), PINE_WINDOW.get());

        registerStorageTypeRenderers();
        registerClientScreens();

        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) -> {
                    if (world == null || pos == null) {
                        return -1;
                    }
                    return BiomeColors.getAverageWaterColor(world, pos);
                }, WOODEN_WATER_CAULDRON, WATERING_CAN
        );
    }

    public static void registerStorageTypeRenderers(){
        StorageBlockEntityRenderer.registerStorageType(StorageTypeRegistry.WHEEL_BARROW, new WheelBarrowRenderer());
        StorageBlockEntityRenderer.registerStorageType(StorageTypeRegistry.FLOWER_POT_SMALL, new FlowerPotSmallRenderer());
        StorageBlockEntityRenderer.registerStorageType(StorageTypeRegistry.CHEESE_RACK, new CheeseRackRenderer());
    }

    private static void registerClientScreens() {
        MenuRegistry.registerScreenFactory(ScreenHandlerRegistry.CHEESE_FORM_SCREEN_HANDLER.get(), CheeseFormGui::new);
        MenuRegistry.registerScreenFactory(ScreenHandlerRegistry.WOODCUTTER_SCREEN_HANDLER.get(), WoodcutterGui::new);
        MenuRegistry.registerScreenFactory(ScreenHandlerRegistry.COOKING_CAULDRON_SCREEN_HANDLER.get(), CookingCauldronGui::new);
        MenuRegistry.registerScreenFactory(ScreenHandlerRegistry.FONDUE_SCREEN_HANDLER.get(), FondueGui::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.SHEARABLE_MEADOW_VAR_COW, ShearableVarCowRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.WATER_BUFFALO, WaterBuffaloRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.PINE_BOAT, context -> new PineBoatRenderer<>(context, false));
        EntityRendererRegistry.register(EntityTypeRegistry.PINE_CHEST_BOAT, context -> new PineBoatRenderer<>(context, true));
    }

    public static void registerEntityModelLayers() {
        EntityModelLayerRegistry.register(FurHelmetModel.LAYER_LOCATION, FurHelmetModel::createBodyLayer);
        EntityModelLayerRegistry.register(FurChestplateModel.LAYER_LOCATION, FurChestplateModel::createBodyLayer);
        EntityModelLayerRegistry.register(FurLeggingsModel.LAYER_LOCATION, FurLeggingsModel::createBodyLayer);
        EntityModelLayerRegistry.register(FurBootsModel.LAYER_LOCATION, FurBootsModel::createBodyLayer);
        EntityModelLayerRegistry.register(SHEARABLE_MEADOW_COW_MODEL_LAYER, WoolyCowModel::createBodyLayer);
        EntityModelLayerRegistry.register(WATER_BUFFALO_MODEL_LAYER, WaterBuffaloModel::getTexturedModelData);
    }
}