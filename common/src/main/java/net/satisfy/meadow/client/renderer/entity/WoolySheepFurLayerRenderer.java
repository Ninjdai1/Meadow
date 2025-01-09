package net.satisfy.meadow.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.meadow.client.model.WoolySheepFurModel;
import net.satisfy.meadow.client.model.WoolySheepModel;
import net.satisfy.meadow.core.entity.WoolySheepEntity;
import net.satisfy.meadow.core.util.MeadowIdentifier;

public class WoolySheepFurLayerRenderer extends RenderLayer<WoolySheepEntity, WoolySheepModel<WoolySheepEntity>> {
    private static final ResourceLocation FLECKED_FUR_LOCATION = new MeadowIdentifier("textures/entity/sheep/flecked_sheep_fur.png");
    private static final ResourceLocation PATCHED_FUR_LOCATION = new MeadowIdentifier("textures/entity/sheep/patched_sheep_fur.png");
    private static final ResourceLocation ROCKY_FUR_LOCATION = new MeadowIdentifier("textures/entity/sheep/rocky_sheep_fur.png");
    private static final ResourceLocation INKY_FUR_LOCATION = new MeadowIdentifier("textures/entity/sheep/inky_sheep_fur.png");
    private final WoolySheepFurModel<WoolySheepEntity> sheepModel;

    public WoolySheepFurLayerRenderer(RenderLayerParent<WoolySheepEntity, WoolySheepModel<WoolySheepEntity>> renderLayerParent, EntityRendererProvider.Context context) {
        super(renderLayerParent);
        this.sheepModel = new WoolySheepFurModel<>(context.bakeLayer(ModelLayers.SHEEP_FUR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, WoolySheepEntity meadowSheep, float f, float g, float h, float j, float k, float l) {
        if (!meadowSheep.isSheared() && !meadowSheep.isInvisible()) {
            ResourceLocation furLocation = getFurLocation(meadowSheep.getSheepTexture());
            float[] fs = meadowSheep.getTextureColor().getTextureDiffuseColors();

            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.sheepModel, furLocation, poseStack, multiBufferSource, i, meadowSheep, f, g, j, k, l, h, fs[0], fs[1], fs[2]);
        }
    }


    private ResourceLocation getFurLocation(WoolySheepEntity.SheepTexture texture) {
        return switch (texture) {
            case PATCHED -> PATCHED_FUR_LOCATION;
            case ROCKY -> ROCKY_FUR_LOCATION;
            case INKY -> INKY_FUR_LOCATION;
            default -> FLECKED_FUR_LOCATION;
        };
    }
}