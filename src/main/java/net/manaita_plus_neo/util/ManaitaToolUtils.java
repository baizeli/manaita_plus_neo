package net.manaita_plus_neo.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.function.BiPredicate;

public class ManaitaToolUtils {

    public static boolean isDoublingEnabled(ItemStack itemStack) {
        return ManaitaItemUtils.getBooleanProperty(itemStack, "Doubling", false);
    }

    public static void setDoublingEnabled(ItemStack itemStack, boolean enabled) {
        ManaitaItemUtils.setBooleanProperty(itemStack, "Doubling", enabled);
    }

    public static int getRange(ItemStack itemStack) {
        return ManaitaItemUtils.getIntProperty(itemStack, "Range", 1);
    }

    public static void setRange(ItemStack itemStack, int range) {
        if (range <= 0) range = 1;
        ManaitaItemUtils.setIntProperty(itemStack, "Range", range);
    }

    public static void performRangeBreak(ItemStack toolItem, Level level, BlockPos pos, Player player, int range, BiPredicate<ItemStack, BlockState> canMineBlock) {
        ManaitaRangeUtils.performRangeBreak(toolItem, level, pos, player, range, canMineBlock);
    }

    public static void destroyBlocksInRange(ItemStack stack, Level level, BlockPos pos, Player player, int range) {
        ManaitaRangeUtils.destroyBlocksInRange(stack, level, pos, player, range);
    }

    public static InteractionResult performAxeRightClick(UseOnContext context) {
        return ManaitaToolModificationUtils.performAxeRightClick(context);
    }

    public static InteractionResult performHoeRightClick(UseOnContext context) {
        return ManaitaToolModificationUtils.performHoeRightClick(context);
    }

    public static InteractionResult performShovelRightClick(UseOnContext context) {
        return ManaitaToolModificationUtils.performShovelRightClick(context);
    }

    public static void toggleEnchantment(ItemStack itemStack, Player player, String itemName) {
        ManaitaEnchantmentUtils.toggleEnchantment(itemStack, player, itemName);
    }

    public static void toggleRange(ItemStack itemStack, Player player, int maxRange, String itemName) {
        int currentRange = getRange(itemStack);
        int newRange = currentRange + 2;
        if (newRange > maxRange) {
            newRange = 1;
        }
        setRange(itemStack, newRange);
        ManaitaMessageUtils.sendRangeChangeMessage(player, itemStack, itemName, newRange);
    }

    public static void handleDropsAndExp(BlockEvent.BreakEvent event, ItemStack toolStack) {
        ManaitaRangeUtils.handleDropsAndExp(event, toolStack);
    }

    public static void handleManaitaKeyPress(ItemStack itemStack, Player player, String itemName) {
        boolean doubling = !isDoublingEnabled(itemStack);
        setDoublingEnabled(itemStack, doubling);
    }

    public static void handleManaitaKeyPressOnClient(ItemStack itemStack, Player player, String itemName) {
        boolean doubling = !isDoublingEnabled(itemStack);
        setDoublingEnabled(itemStack, doubling);
        ManaitaMessageUtils.sendDoublingChangeMessageOnClient(player, itemStack, itemName);
    }

    public static void performSweepAttack(Player player, int sweep) {
        for (int i1 = 0; i1 < sweep; i1++) {
            Vec3 vec3 = player.getLookAngle();
            AABB aabb = player.getBoundingBox().expandTowards(3.0D, 3.0D, 3.0D).move(vec3.x * i1, vec3.y * i1, vec3.z * i1);
            for (Entity entity1 : player.level().getEntities(player, aabb, (p_20434_) -> true)) {
                if (entity1 instanceof LivingEntity living) {
                    if (!player.level().isClientSide) {
                        living.hurt(living.damageSources().playerAttack(player), 10000.0F);
                        living.die(living.damageSources().playerAttack(player));
                        living.setHealth(0F);
                        living.deathTime = 15;
                    }
                    for (int i = 0; i < 5; i++) {
                        living.handleEntityEvent((byte) 2);
                    }
                    for (int i = 47; i < 53; i++) {
                        living.handleEntityEvent((byte) i);
                    }
                    living.handleEntityEvent((byte) 3);
                }
            }
            double d0 = (-Mth.sin(player.getYRot() * ((float)Math.PI / 180F)));
            double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0 + vec3.x * i1, player.getY(0.5D) + vec3.y * i1, player.getZ() + d1+ vec3.z * i1, 0, d0, 0.0D, d1, 0.0D);
            }
            player.level().playSound( null, player.getX() + d0 + vec3.x * i1, player.getY(0.5D) + vec3.y * i1, player.getZ() + d1+ vec3.z * i1,  SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
        }
    }
}