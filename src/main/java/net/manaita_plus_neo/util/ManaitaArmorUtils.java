package net.manaita_plus_neo.util;

import net.manaita_plus_neo.item.armor.ManaitaBoots;
import net.manaita_plus_neo.item.armor.ManaitaChestplate;
import net.manaita_plus_neo.item.armor.ManaitaHelmet;
import net.manaita_plus_neo.item.armor.ManaitaLeggings;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

public class ManaitaArmorUtils {

    public static boolean isWearingManaitaArmor(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        return isManaitaArmor(helmet) || isManaitaArmor(chestplate) ||
                isManaitaArmor(leggings) || isManaitaArmor(boots);
    }

    public static boolean isManaitaArmor(ItemStack stack) {
        if (stack.isEmpty()) return false;

        return stack.getItem() instanceof ManaitaHelmet || stack.getItem() instanceof ManaitaChestplate ||
                stack.getItem() instanceof ManaitaLeggings || stack.getItem() instanceof ManaitaBoots;
    }
    
    // 头盔/夜视
    public static boolean getNightVision(ItemStack itemStack) {
        return ManaitaItemUtils.getBooleanProperty(itemStack, "NightVision", false);
    }
    
    public static void setNightVision(ItemStack itemStack, boolean enabled) {
        ManaitaItemUtils.setBooleanProperty(itemStack, "NightVision", enabled);
    }
    
    // 护腿/隐身
    public static boolean getInvisibility(ItemStack itemStack) {
        return ManaitaItemUtils.getBooleanProperty(itemStack, "Invisibility", false);
    }
    
    public static void setInvisibility(ItemStack itemStack, boolean enabled) {
        ManaitaItemUtils.setBooleanProperty(itemStack, "Invisibility", enabled);
    }
    
    // 靴子/速度
    public static int getSpeed(ItemStack itemStack) {
        return ManaitaItemUtils.getIntProperty(itemStack, "Speed", 1);
    }
    
    public static void setSpeed(ItemStack itemStack, int speed) {
        ManaitaItemUtils.setIntProperty(itemStack, "Speed", speed);
    }

    public static void handleNightVisionKeyPressOnClient(ItemStack itemStack, Player player) {
        boolean nightVision = !getNightVision(itemStack);
        setNightVision(itemStack, nightVision);
        ManaitaMessageUtils.sendArmorNightVisionChangeMessage(player, itemStack, I18n.get("item.manaita_plus_neo.manaita_helmet"), nightVision);
    }
    
    public static void handleInvisibilityKeyPressOnClient(ItemStack itemStack, Player player) {
        boolean invisibility = !getInvisibility(itemStack);
        setInvisibility(itemStack, invisibility);
        ManaitaMessageUtils.sendArmorInvisibilityChangeMessage(player, itemStack, I18n.get("item.manaita_plus_neo.manaita_leggings"), invisibility);
    }
    
    public static void handleSpeedKeyPressOnClient(ItemStack itemStack, Player player) {
        int speed = getSpeed(itemStack);
        speed = (speed % 9) + 1;
        setSpeed(itemStack, speed);
        ManaitaMessageUtils.sendArmorSpeedChangeMessage(player, itemStack, I18n.get("item.manaita_plus_neo.manaita_boots"), speed);
    }
}