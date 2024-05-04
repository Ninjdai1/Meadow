package net.satisfyu.meadow.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import de.cristelknight.doapi.client.ClientUtil;
import de.cristelknight.doapi.client.render.block.storage.StorageTypeRenderer;
import de.cristelknight.doapi.common.block.entity.StorageBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class WheelBarrowRenderer implements StorageTypeRenderer {

    @Override
    public void render(StorageBlockEntity storageBlockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, NonNullList<ItemStack> itemStacks) {
        ItemStack stack = itemStacks.get(0);
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            poseStack.translate(0f, 0.625f, 0f);
            ClientUtil.renderBlock(state, poseStack, multiBufferSource, storageBlockEntity);
        }
    }
}