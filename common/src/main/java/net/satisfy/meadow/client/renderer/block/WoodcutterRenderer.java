package net.satisfy.meadow.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.satisfy.meadow.core.block.WoodcutterBlock;
import net.satisfy.meadow.core.block.entity.WoodcutterBlockEntity;
import org.joml.Quaternionf;

public class WoodcutterRenderer implements BlockEntityRenderer<WoodcutterBlockEntity> {

    public WoodcutterRenderer() {
    }

    @Override
    public void render(WoodcutterBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (blockEntity.hasAxe()) {
            poseStack.pushPose();

            poseStack.translate(0.5, 1.56, 0.5);
            Direction facing = blockEntity.getBlockState().getValue(WoodcutterBlock.FACING);
            switch (facing) {
                case NORTH -> poseStack.mulPose(new Quaternionf().rotateY(0));
                case SOUTH -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
                case WEST -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(90)));
                case EAST -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-90)));
            }

            poseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(180)));
            poseStack.scale(2F, 2F, 2F);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    blockEntity.getAxe(),
                    ItemDisplayContext.GROUND,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    0
            );

            poseStack.popPose();
        }
    }
}
