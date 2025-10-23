package net.manaita_plus_neo.event;

import net.manaita_plus_neo.item.armor.ManaitaHelmet;
import net.manaita_plus_neo.item.armor.ManaitaLeggings;
import net.manaita_plus_neo.item.armor.ManaitaBoots;
import net.manaita_plus_neo.util.ManaitaArmorUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class ManaitaArmorEvents {
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        
        // 盔甲/飞行
        if (ManaitaArmorUtils.isWearingManaitaArmor(player)) {
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            }
        } else {
            if (player.getAbilities().mayfly && !player.isCreative() && !player.isSpectator()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
        
        // 头盔/夜视
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.getItem() instanceof ManaitaHelmet && ManaitaArmorUtils.getNightVision(helmet)) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 2, 0, false, false, false));
        }
        
        // 护腿/隐身
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        if (leggings.getItem() instanceof ManaitaLeggings && ManaitaArmorUtils.getInvisibility(leggings)) {
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 0, false, false));
        }
        
        // 靴子/速度
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.getItem() instanceof ManaitaBoots && !player.isSpectator()) {
            int speed = ManaitaArmorUtils.getSpeed(boots);
            float speedValue = 0.1F * speed;
            player.getAbilities().setWalkingSpeed(speedValue);
            player.getAbilities().setFlyingSpeed(speedValue / 2);
            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speedValue);
        }
    }
}