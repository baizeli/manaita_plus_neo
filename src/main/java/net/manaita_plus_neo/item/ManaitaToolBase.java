package net.manaita_plus_neo.item;

import net.manaita_plus_neo.item.data.IManaitaPlusKey;
import net.manaita_plus_neo.util.ManaitaToolUtils;
import net.manaita_plus_neo.util.ManaitaCommonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.BiPredicate;

public abstract class ManaitaToolBase implements IManaitaPlusKey {
    protected final String toolType;

    public ManaitaToolBase(String toolType) {
        this.toolType = toolType;
    }

    public int getDamage(ItemStack stack) {
        return 0;
    }
    
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return Float.MAX_VALUE;
    }

    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return ManaitaCommonUtils.isCorrectToolForDrops(state, toolType);
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        int range = ManaitaToolUtils.getRange(stack);

        if (miningEntity instanceof Player player) {
            ManaitaToolUtils.performRangeBreak(stack, level, pos, player, range, 
            getMinePredicate());
        }
        
        return true;
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return true;
    }

    public void appendHoverText(ItemStack stack, TooltipFlag flag, List<Component> tooltip) {
        ManaitaCommonUtils.addCommonTooltip(stack, tooltip, flag, toolType);
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        ManaitaToolUtils.handleManaitaKeyPress(itemStack, player, getItemName());
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        ManaitaToolUtils.handleManaitaKeyPressOnClient(itemStack, player, getItemName());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.isShiftKeyDown()) {
                ManaitaToolUtils.toggleRange(itemInHand, player, getMaxRange(), getItemName());
            } else {
                ManaitaToolUtils.toggleEnchantment(itemInHand, player, getItemName());
            }
        }
        return InteractionResultHolder.pass(itemInHand);
    }

    public abstract String getItemName();
    public abstract int getMaxRange();
    
    protected BiPredicate<ItemStack, BlockState> getMinePredicate() {
        return this::isCorrectToolForDrops;
    }
}