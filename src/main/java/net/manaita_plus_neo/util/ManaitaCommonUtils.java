package net.manaita_plus_neo.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ManaitaCommonUtils {

    public static void addCommonTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag, String toolType) {
        ManaitaMessageUtils.sendCommonTooltipMessage(stack, tooltip, flag, toolType);
    }

    public static void addWeaponTooltip(ItemStack stack, List<Component> tooltip, String weaponType) {
        ManaitaMessageUtils.sendWeaponTooltipMessage(stack, tooltip, weaponType);
    }
    
    public static void addGodSwordTooltip(ItemStack stack, List<Component> tooltip) {
        ManaitaMessageUtils.sendGodSwordTooltipMessage(stack, tooltip);
    }

    public static void addArmorTooltip(ItemStack stack, List<Component> tooltip) {
        ManaitaMessageUtils.sendArmorTooltipMessage(stack, tooltip);
    }

    public static void addHelmetTooltip(ItemStack stack, List<Component> tooltip) {
        ManaitaMessageUtils.sendHelmetTooltipMessage(stack, tooltip);
    }

    public static void addLeggingsTooltip(ItemStack stack, List<Component> tooltip) {
        ManaitaMessageUtils.sendLeggingsTooltipMessage(stack, tooltip);
    }

    public static void addBootsTooltip(ItemStack stack, List<Component> tooltip) {
        ManaitaMessageUtils.sendBootsTooltipMessage(stack, tooltip);
    }

    public static boolean isCorrectToolForDrops(BlockState state, String toolType) {
        switch (toolType.toLowerCase()) {
            case "axe":
                return state.is(BlockTags.MINEABLE_WITH_AXE);
            case "pickaxe":
                return state.is(BlockTags.MINEABLE_WITH_PICKAXE);
            case "shovel":
                return state.is(BlockTags.MINEABLE_WITH_SHOVEL);
            case "hoe":
                return state.is(BlockTags.MINEABLE_WITH_HOE);
            case "paxel":
                return true;
            case "shears":
                return state.is(Blocks.COBWEB) || state.is(Blocks.REDSTONE_WIRE) || state.is(Blocks.TRIPWIRE) ||
                state.is(BlockTags.LEAVES) || state.is(BlockTags.WOOL) || state.is(Blocks.VINE) || state.is(Blocks.GLOW_LICHEN);
            default:
                return false;
        }
    }
}