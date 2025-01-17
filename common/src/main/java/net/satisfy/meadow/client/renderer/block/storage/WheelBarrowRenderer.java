package net.satisfy.meadow.client.renderer.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.meadow.client.util.ClientUtil;
import net.satisfy.meadow.core.block.entity.StorageBlockEntity;

public class WheelBarrowRenderer implements StorageTypeRenderer {
    @Override
    public void render(StorageBlockEntity storageBlockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, NonNullList<ItemStack> itemStacks) {
        ItemStack stack = itemStacks.get(0);
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            poseStack.translate(-0.5f, 0.5f, -0.5f);
            ClientUtil.renderBlock(state, poseStack, multiBufferSource, storageBlockEntity);
        }
    }
}