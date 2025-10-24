package net.manaita_plus_neo.mixin;

import net.manaita_plus_neo.item.ModItems;
import net.manaita_plus_neo.util.ManaitaArmorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player) {
            if (ManaitaArmorUtils.isWearingManaitaArmor(player) || player.getInventory().items.stream().anyMatch(stack -> !stack.isEmpty() && stack.is(ModItems.MANAITA_SWORD_GOD.get()))) {
                cir.cancel();
            }
        }
    }
}