package net.manaita_plus_neo.item.weapon;

import net.manaita_plus_neo.common.network.client.MessageKey;
import net.manaita_plus_neo.item.ManaitaWeaponBase;
import net.manaita_plus_neo.util.EntityUtils;
import net.manaita_plus_neo.util.ManaitaToolUtils;
import net.manaita_plus_neo.util.ManaitaSwordUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.network.PacketDistributor;

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
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        entity.levelCallback = EntityInLevelCallback.NULL;
        entity.gameEvent(GameEvent.ENTITY_DIE);
        entity.isAddedToLevel = false;
        if (entity instanceof LivingEntity living) {
            EntityUtils.setHealth(living, 0F);
            living.deathTime = 20;
            living.hurtTime = 20;
            living.dead = true;
            living.brain.clearMemories();
        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        byte keyType = 0;
        MessageKey packet = new MessageKey(keyType);
        PacketDistributor.sendToServer(packet);

        return super.use(level, player, hand);
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