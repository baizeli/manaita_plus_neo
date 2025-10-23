package net.manaita_plus_neo.mixin.client;

import net.manaita_plus_neo.item.armor.ManaitaHelmet;
import net.manaita_plus_neo.util.ManaitaArmorUtils;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class NightVisionMixin {

    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionScale(LivingEntity livingEntity, float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (livingEntity instanceof Player player) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

            if (helmet.getItem() instanceof ManaitaHelmet && ManaitaArmorUtils.getNightVision(helmet)) {
                MobEffectInstance effect = player.getEffect(MobEffects.NIGHT_VISION);

                if (effect != null) {
                    cir.setReturnValue(1.0F);
                    cir.cancel();
                }
            }
        }
    }
}