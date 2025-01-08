package net.satisfy.meadow.core.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.meadow.core.registry.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "isExceptionForConnection", at = @At("HEAD"), cancellable = true)
    private static void checkCannotConnect(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(TagRegistry.CAN_NOT_CONNECT)) cir.setReturnValue(true);
    }
}
