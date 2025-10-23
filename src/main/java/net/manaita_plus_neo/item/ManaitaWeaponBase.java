package net.manaita_plus_neo.item;

import net.manaita_plus_neo.item.data.IManaitaPlusKey;
import net.manaita_plus_neo.util.ManaitaCommonUtils;
import net.manaita_plus_neo.util.ManaitaTextFormatter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import java.util.List;

public abstract class ManaitaWeaponBase extends Item implements IManaitaPlusKey {
    
    protected final String weaponType;

    public ManaitaWeaponBase(Item.Properties properties, String weaponType) {
        super(properties);
        this.weaponType = weaponType;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ManaitaCommonUtils.addWeaponTooltip(stack, tooltip, weaponType);
    }
    
    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(ManaitaTextFormatter.ManaitaText.manaita_infinity.formatting(getItemName()));
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player) {}
    
    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {}
    
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    protected abstract String getItemName();
}