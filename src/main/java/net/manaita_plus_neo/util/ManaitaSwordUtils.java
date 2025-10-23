package net.manaita_plus_neo.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ManaitaSwordUtils {
    
    public static int getSweep(ItemStack itemStack) {
        return ManaitaItemUtils.getIntProperty(itemStack, "Sweep", 1);
    }
    
    public static void setSweep(ItemStack itemStack, int sweep) {
        ManaitaItemUtils.setIntProperty(itemStack, "Sweep", sweep);
    }
    
    public static void handleSwordSweepChangeOnClient(ItemStack itemStack, Player player, String itemName, int newSweepValue) {
        ManaitaMessageUtils.sendSwordSweepChangeMessageOnClient(player, itemStack, itemName, newSweepValue);
    }
    
    public static int getNextSweepValue(int currentSweep) {
        switch (currentSweep) {
            case 1: return 4;
            case 4: return 16;
            case 16: return 64;
            case 64: return 256;
            case 256: return 1024;
            case 1024: return 1;
            default: return 1;
        }
    }
}