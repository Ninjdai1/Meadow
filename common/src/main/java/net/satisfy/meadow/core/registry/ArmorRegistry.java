package net.satisfy.meadow.core.registry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.meadow.core.item.FurBootsItem;
import net.satisfy.meadow.core.item.FurChestItem;
import net.satisfy.meadow.core.item.FurHelmetItem;
import net.satisfy.meadow.core.item.FurLegsItem;


public class ArmorRegistry {
    public static boolean setBonusActive = false;

    public static void checkArmorSet(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        setBonusActive = helmet.getItem() instanceof FurHelmetItem &&
                chestplate.getItem() instanceof FurChestItem &&
                leggings.getItem() instanceof FurLegsItem &&
                boots.getItem() instanceof FurBootsItem;
    }
}
