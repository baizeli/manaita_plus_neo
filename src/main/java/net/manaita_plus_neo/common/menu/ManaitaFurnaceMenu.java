package net.manaita_plus_neo.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.Container;

public class ManaitaFurnaceMenu extends AbstractFurnaceMenu {
    public ManaitaFurnaceMenu(int containerId, Inventory playerInventory) {
        super(ModMenuTypes.FURNACE_MANAITA.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, containerId, playerInventory);
    }

    public ManaitaFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.FURNACE_MANAITA.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, containerId, playerInventory, container, data);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return super.quickMoveStack(player, index);
    }
}