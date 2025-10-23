package net.manaita_plus_neo.item.weapon;

import net.manaita_plus_neo.item.ManaitaWeaponBase;
import net.manaita_plus_neo.util.ManaitaToolUtils;
import net.manaita_plus_neo.util.ManaitaSwordUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

public class ManaitaSword extends ManaitaWeaponBase {
    
    public ManaitaSword(Tier tier, Item.Properties properties) {
        super(properties, "sword");
    }

    @Override
    @SuppressWarnings("removal")
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            int sweep = ManaitaSwordUtils.getSweep(stack);
            ManaitaToolUtils.performSweepAttack(player, sweep);
        }
        return false;
    }
    
    @Override
    protected String getItemName() {
        return I18n.get("item.manaita_plus_neo.manaita_sword");
    }
    
    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        int sweep = ManaitaSwordUtils.getSweep(itemStack);
        sweep = ManaitaSwordUtils.getNextSweepValue(sweep);
        ManaitaSwordUtils.setSweep(itemStack, sweep);
    }
    
    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        int sweep = ManaitaSwordUtils.getSweep(itemStack);
        sweep = ManaitaSwordUtils.getNextSweepValue(sweep);
        ManaitaSwordUtils.setSweep(itemStack, sweep);

        ManaitaSwordUtils.handleSwordSweepChangeOnClient(
            itemStack, 
            player, 
            I18n.get("item.manaita_plus_neo.manaita_sword"), 
            sweep
        );
    }
}