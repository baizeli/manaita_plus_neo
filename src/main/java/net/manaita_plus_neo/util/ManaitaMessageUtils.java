package net.manaita_plus_neo.util;

import net.manaita_plus_neo.util.ManaitaTextFormatter.ManaitaText;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ManaitaMessageUtils {
    
    public static void sendRangeChangeMessage(Player player, ItemStack itemStack, String itemName, int newRange) {
        String rangeText = I18n.get("mode.range.name");
        player.sendSystemMessage(Component.literal(
            ManaitaText.manaita_mode.formatting(
                "[" + itemName + "] " + rangeText + ": " + newRange + "x" + newRange + "x" + newRange
            )
        ));
    }
    
    public static void sendEnchantmentChangeMessage(Player player, ItemStack itemStack, String itemName, boolean isSilkTouch) {
        if (isSilkTouch) {
            String s = I18n.get("enchantments.silktouch");
            String enchantmentText = I18n.get("info.enchantment");
            player.sendSystemMessage(Component.literal(
                ManaitaText.manaita_enchantment.formatting(itemName + enchantmentText + ": " + s)
            ));
        } else {
            String s = I18n.get("enchantments.fortune");
            String enchantmentText = I18n.get("info.enchantment");
            player.sendSystemMessage(Component.literal(
                ManaitaText.manaita_enchantment.formatting(itemName + enchantmentText + ": " + s)
            ));
        }
    }
    
    public static void sendDoublingChangeMessageOnClient(Player player, ItemStack itemStack, String itemName) {
        boolean doubling = ManaitaToolUtils.isDoublingEnabled(itemStack);
        String doublingText = I18n.get("mode.doubling");
        String statusText = doubling ? I18n.get("info.on") : I18n.get("info.off");
        player.displayClientMessage(Component.literal(
            ManaitaText.manaita_mode.formatting(
                "[" + itemName + "] " + doublingText + ": " + statusText
            )
        ), true);
    }
    
    public static void sendSwordSweepChangeMessageOnClient(Player player, ItemStack itemStack, String itemName, int newSweepValue) {
        String sweepText = I18n.get("mode.manaita_sword");
        player.displayClientMessage(Component.literal(
            ManaitaText.manaita_mode.formatting(
                "[" + itemName + "] " + sweepText + ": " + newSweepValue
            )
        ), true);
    }

    public static void sendArmorNightVisionChangeMessage(Player player, ItemStack itemStack, String itemName, boolean nightVision) {
        String nightVisionText = I18n.get("mode.nightvision");
        String statusText = nightVision ? I18n.get("info.on") : I18n.get("info.off");
        player.displayClientMessage(Component.literal(
            ManaitaText.manaita_mode.formatting(
                "[" + itemName + "] " + nightVisionText + ": " + statusText
            )
        ), true);
    }
    
    public static void sendArmorInvisibilityChangeMessage(Player player, ItemStack itemStack, String itemName, boolean invisibility) {
        String invisibilityText = I18n.get("mode.invisibility");
        String statusText = invisibility ? I18n.get("info.on") : I18n.get("info.off");
        player.displayClientMessage(Component.literal(
            ManaitaText.manaita_mode.formatting(
                "[" + itemName + "] " + invisibilityText + ": " + statusText
            )
        ), true);
    }
    
    public static void sendArmorSpeedChangeMessage(Player player, ItemStack itemStack, String itemName, int speed) {
        String speedText = I18n.get("mode.speed");
        String displaySpeed = (speed == 1) ? I18n.get("info.default") : String.valueOf(speed);
        player.displayClientMessage(Component.literal(
            ManaitaText.manaita_mode.formatting(
                "[" + itemName + "] " + speedText + ": " + displaySpeed
            )
        ), true);
    }

    public static void sendCommonTooltipMessage(ItemStack stack, List<Component> tooltip, TooltipFlag flag, String toolType) {
        int range = ManaitaToolUtils.getRange(stack);
        String rangeText = I18n.get("mode.manaita_tool");
        String sizeText = I18n.get("mode.range.name");
        tooltip.add(Component.literal(ManaitaText.manaita_mode.formatting("[" + sizeText + "] " + rangeText + ": " + range + "x" + range + "x" + range)));
        boolean doubling = ManaitaToolUtils.isDoublingEnabled(stack);
        String doublingText = I18n.get("mode.doubling");
        String statusText = doubling ? I18n.get("info.on") : I18n.get("info.off");
        tooltip.add(Component.literal(ManaitaText.manaita_mode.formatting("[" + doublingText + "] " + statusText)));
    }
    
    public static void sendWeaponTooltipMessage(ItemStack stack, List<Component> tooltip, String weaponType) {
        if ("sword".equals(weaponType)) {
            int sweep = ManaitaSwordUtils.getSweep(stack);
            String sweepText = I18n.get("mode.manaita_sword");
            tooltip.add(Component.literal(ManaitaText.manaita_mode.formatting(sweepText + ": " + sweep)));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal(ManaitaText.manaita_infinity.formatting(I18n.get("info.attack"))));
    }
    
    public static void sendArmorTooltipMessage(ItemStack stack, List<Component> tooltip) {
        tooltip.add(Component.literal(ManaitaText.manaita_infinity.formatting(I18n.get("info.armor"))));
    }
    
    public static void sendHelmetTooltipMessage(ItemStack stack, List<Component> tooltip) {
        sendArmorTooltipMessage(stack, tooltip);
        boolean nightVision = ManaitaArmorUtils.getNightVision(stack);
        String statusText = nightVision ? I18n.get("info.on") : I18n.get("info.off");
        tooltip.add(Component.literal(ManaitaText.manaita_mode.formatting(
            I18n.get("mode.nightvision") + ": " + statusText)
        ));
    }
    
    public static void sendLeggingsTooltipMessage(ItemStack stack, List<Component> tooltip) {
        sendArmorTooltipMessage(stack, tooltip);
        boolean invisibility = ManaitaArmorUtils.getInvisibility(stack);
        String statusText = invisibility ? I18n.get("info.on") : I18n.get("info.off");
        tooltip.add(Component.literal(ManaitaText.manaita_mode.formatting(
            I18n.get("mode.invisibility") + ": " + statusText)
        ));
    }
    
    public static void sendBootsTooltipMessage(ItemStack stack, List<Component> tooltip) {
        sendArmorTooltipMessage(stack, tooltip);
        int speed = ManaitaArmorUtils.getSpeed(stack);
        String displaySpeed = (speed == 1) ? I18n.get("info.default") : String.valueOf(speed);
        tooltip.add(Component.literal(ManaitaText.manaita_mode.formatting(
            I18n.get("mode.speed") + ": " + displaySpeed)
        ));
    }
    
    public static void sendGodSwordTooltipMessage(ItemStack stack, List<Component> tooltip) {
        tooltip.add(Component.literal(ManaitaText.manaita_mode.formatting(
            I18n.get("mode.remove.name") + ":" + I18n.get("info.on")
        )));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal(ManaitaText.manaita_enchantment.formatting(
            I18n.get("info.item.manaita_sword_god.1")
        )));
    }
}