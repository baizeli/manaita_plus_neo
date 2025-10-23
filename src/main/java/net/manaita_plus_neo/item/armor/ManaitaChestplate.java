package net.manaita_plus_neo.item.armor;

import net.manaita_plus_neo.util.ManaitaCommonUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ManaitaChestplate extends ArmorItem {
    public ManaitaChestplate(Properties properties) {
        super(ManaitaArmorMaterial.MANAITA, Type.CHESTPLATE, properties);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ManaitaCommonUtils.addArmorTooltip(stack, tooltip);
    }

/*     @Override
    public Component getName(ItemStack stack) {
        return Component.literal(ManaitaTextFormatter.ManaitaText.manaita_infinity.formatting(I18n.get("item.manaita_plus_neo.manaita_chestplate")));
    } */
}