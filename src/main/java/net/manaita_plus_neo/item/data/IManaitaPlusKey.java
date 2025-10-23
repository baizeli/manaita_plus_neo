package net.manaita_plus_neo.item.data;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IManaitaPlusKey {
    void onManaitaKeyPress(ItemStack paramItemStack, Player paramEntityPlayer);
    void onManaitaKeyPressOnClient(ItemStack paramItemStack, Player paramEntityPlayer);
}