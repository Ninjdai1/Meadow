package net.satisfy.meadow.item.armor;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.satisfy.meadow.registry.ArmorRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class FurLegs extends CustomArmorItem {
    public FurLegs(ArmorMaterial material, Properties settings) {
        super(material, Type.LEGGINGS, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, TooltipFlag context) {
        if(world != null && world.isClientSide()){
            ArmorRegistry.appendToolTip(tooltip);
        }
    }
}