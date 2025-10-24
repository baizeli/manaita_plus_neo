package net.manaita_plus_neo.item.data;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface IManaitaPlusDestroy {
    boolean accept(BlockState state);
    int getRange(ItemStack itemStack);
}
