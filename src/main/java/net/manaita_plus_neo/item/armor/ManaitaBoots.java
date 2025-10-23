package net.manaita_plus_neo.item.armor;

import net.manaita_plus_neo.item.data.IManaitaPlusKey;
import net.manaita_plus_neo.util.ManaitaArmorUtils;
import net.manaita_plus_neo.util.ManaitaCommonUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ManaitaBoots extends ArmorItem implements IManaitaPlusKey {
    public ManaitaBoots(Properties properties) {
        super(ManaitaArmorMaterial.MANAITA, Type.BOOTS, properties);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ManaitaCommonUtils.addBootsTooltip(stack, tooltip);
    }
    
    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        if (!player.isShiftKeyDown()) {
            int speed = ManaitaArmorUtils.getSpeed(itemStack);
            speed = (speed % 9) + 1;
            ManaitaArmorUtils.setSpeed(itemStack, speed);
        }
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        if (!player.isShiftKeyDown()) {
            ManaitaArmorUtils.handleSpeedKeyPressOnClient(itemStack, player);
        }
    }

/*     @Override
    public Component getName(ItemStack stack) {
        return Component.literal(ManaitaTextFormatter.ManaitaText.manaita_infinity.formatting(I18n.get("item.manaita_plus_neo.manaita_boots")));
    } */
}