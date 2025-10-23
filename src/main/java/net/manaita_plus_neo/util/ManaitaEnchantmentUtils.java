package net.manaita_plus_neo.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class ManaitaEnchantmentUtils {
    
    public static void toggleEnchantment(ItemStack itemStack, Player player, String itemName) {
        var currentEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemStack);
        var mutableEnchantments = new ItemEnchantments.Mutable(currentEnchantments);

        Holder<Enchantment> silkTouchHolder = player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH);
        Holder<Enchantment> fortuneHolder = player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);

        if (!hasSilkTouch(currentEnchantments)) {
            mutableEnchantments.removeIf(holder -> holder.equals(fortuneHolder));
            mutableEnchantments.set(silkTouchHolder, 1);
            EnchantmentHelper.setEnchantments(itemStack, mutableEnchantments.toImmutable());
            ManaitaMessageUtils.sendEnchantmentChangeMessage(player, itemStack, itemName, true);
        } else {
            mutableEnchantments.removeIf(holder -> holder.equals(silkTouchHolder));
            mutableEnchantments.set(fortuneHolder, 10);
            EnchantmentHelper.setEnchantments(itemStack, mutableEnchantments.toImmutable());
            ManaitaMessageUtils.sendEnchantmentChangeMessage(player, itemStack, itemName, false);
        }
    }
    
    public static boolean hasSilkTouch(ItemEnchantments enchantments) {
        return enchantments.keySet().stream().anyMatch(holder -> holder.is(Enchantments.SILK_TOUCH));
    }
}