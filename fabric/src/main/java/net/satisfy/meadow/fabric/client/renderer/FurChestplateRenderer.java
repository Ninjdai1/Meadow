package net.satisfy.meadow.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.meadow.core.item.FurChestItem;
import net.satisfy.meadow.core.registry.ArmorRegistry;

public class FurChestplateRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        if (stack.getItem() instanceof FurChestItem chestplate) {
            Model model = ArmorRegistry.getChestplateModel(chestplate, contextModel.body, contextModel.leftArm, contextModel.rightArm, contextModel.leftLeg, contextModel.rightLeg);

            model.renderToBuffer(matrices, vertexConsumers.getBuffer(model.renderType(chestplate.getChestplateTexture())), light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }
}

