package net.manaita_plus_neo.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;

public class ManaitaItemUtils {

    public static CompoundTag getOrCreateTag(ItemStack itemStack) {
        var customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            return customData.getUnsafe().copy();
        }
        return new CompoundTag();
    }

    public static void saveTag(ItemStack itemStack, CompoundTag tag) {
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static boolean getBooleanProperty(ItemStack itemStack, String propertyName, boolean defaultValue) {
        var customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            var tag = customData.getUnsafe();
            if (tag.contains(propertyName)) {
                return tag.getBoolean(propertyName);
            }
        }
        return defaultValue;
    }

    public static void setBooleanProperty(ItemStack itemStack, String propertyName, boolean value) {
        var tag = getOrCreateTag(itemStack);
        tag.putBoolean(propertyName, value);
        saveTag(itemStack, tag);
    }
    
    public static int getIntProperty(ItemStack itemStack, String propertyName, int defaultValue) {
        var customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            var tag = customData.getUnsafe();
            if (tag.contains(propertyName)) {
                return tag.getInt(propertyName);
            }
        }
        return defaultValue;
    }

    public static void setIntProperty(ItemStack itemStack, String propertyName, int value) {
        var tag = getOrCreateTag(itemStack);
        tag.putInt(propertyName, value);
        saveTag(itemStack, tag);
    }
}