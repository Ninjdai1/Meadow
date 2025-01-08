package net.satisfy.meadow.core.mixin.variant.client;

import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.satisfy.meadow.core.entity.var.SheepVar;
import net.satisfy.meadow.core.util.MeadowIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepRenderer.class)
public class SheepRendererMixin {

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Sheep;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void onGetTexture(Sheep sheep, CallbackInfoReturnable<ResourceLocation> cir) {
        SheepVar var = SheepVar.getVariant(sheep);
        if(var.equals(SheepVar.DEFAULT)) return;
        cir.setReturnValue(new MeadowIdentifier(String.format("textures/entity/sheep/%s_sheep.png", var.getSerializedName())));
    }
}
