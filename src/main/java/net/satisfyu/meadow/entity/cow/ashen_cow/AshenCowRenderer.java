package net.satisfyu.meadow.entity.cow.ashen_cow;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.satisfyu.meadow.MeadowClient;
import net.satisfyu.meadow.entity.ModEntities;
import net.satisfyu.meadow.entity.cow.EyeBlinkRenderer;
import net.satisfyu.meadow.entity.cow.MeadowCowEntityMdodel;

import static net.satisfyu.meadow.Meadow.MOD_ID;

public class AshenCowRenderer extends MobEntityRenderer<AshenCowEntity, MeadowCowEntityMdodel<AshenCowEntity>> {

    private static final Identifier TEXTURE = new Identifier(MOD_ID, "textures/entity/cow/ashen_cow.png");
    public AshenCowRenderer(EntityRendererFactory.Context context) {
        super(context, new MeadowCowEntityMdodel<>(context.getPart(MeadowClient.ASHEN_COW_MODEL_LAYER)), 0.7f);
        this.addFeature(new EyeBlinkRenderer<>(this, "ashen_cow"));
    }

    @Override
    public Identifier getTexture(AshenCowEntity cowEntity) {
        return TEXTURE;
    }


}
