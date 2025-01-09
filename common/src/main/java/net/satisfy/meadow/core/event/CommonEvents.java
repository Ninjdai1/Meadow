package net.satisfy.meadow.core.event;

import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.EventResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.satisfy.meadow.core.entity.WoolySheepEntity;
import net.satisfy.meadow.core.registry.ObjectRegistry;

public class CommonEvents {

    public static void init() {
        new CommonEvents().registerEvents();
    }

    public void registerEvents() {
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (!player.level().isClientSide() &&
                    entity instanceof WoolySheepEntity woolySheep &&
                    player.getItemInHand(hand).is(Items.SHEARS) &&
                    woolySheep.readyForShearing()) {

                WoolySheepEntity.SheepTexture texture = woolySheep.getSheepTexture();
                Item woolItem = getWoolItemByTexture(texture);

                if (woolItem != null) {
                    int count = 1 + woolySheep.getRandom().nextInt(3);
                    for (int i = 0; i < count; i++) {
                        ItemStack woolStack = new ItemStack(woolItem);
                        ItemEntity itemEntity = new ItemEntity(
                                player.level(),
                                woolySheep.getX(),
                                woolySheep.getY() + 1.0D,
                                woolySheep.getZ(),
                                woolStack
                        );
                        itemEntity.setDeltaMovement(
                                itemEntity.getDeltaMovement().add(
                                        (woolySheep.getRandom().nextFloat() - woolySheep.getRandom().nextFloat()) * 0.1F,
                                        woolySheep.getRandom().nextFloat() * 0.05F,
                                        (woolySheep.getRandom().nextFloat() - woolySheep.getRandom().nextFloat()) * 0.1F
                                )
                        );
                        player.level().addFreshEntity(itemEntity);
                    }
                    woolySheep.setSheared(true);

                    ItemStack shears = player.getItemInHand(hand);
                    shears.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));

                    return EventResult.pass();
                }
            }
            return EventResult.pass();
        });
    }

    private Item getWoolItemByTexture(WoolySheepEntity.SheepTexture texture) {
        return switch (texture) {
            case PATCHED -> ObjectRegistry.PATCHED_WOOL.get().asItem();
            case ROCKY -> ObjectRegistry.ROCKY_WOOL.get().asItem();
            case INKY -> ObjectRegistry.INKY_WOOL.get().asItem();
            case FUZZY -> Items.WHITE_WOOL;
            case LONG_NOSED -> ObjectRegistry.HIGHLAND_WOOL.get().asItem();
            default -> ObjectRegistry.FLECKED_WOOL.get().asItem();
        };
    }
}
