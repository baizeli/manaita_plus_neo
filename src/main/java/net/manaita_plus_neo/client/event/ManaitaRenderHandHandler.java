package net.manaita_plus_neo.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.item.weapon.ManaitaSwordGod;

@EventBusSubscriber(modid = ManaitaPlusNeo.MOD_ID, value = Dist.CLIENT)
public class ManaitaRenderHandHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        ItemStack item = event.getItemStack();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == event.getHand()) {
            if (item.getItem() instanceof ManaitaSwordGod && item.getUseAnimation() == UseAnim.BLOCK) {
                HumanoidArm humanoidarm = event.getHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                boolean flag = humanoidarm == HumanoidArm.RIGHT;

                int side = flag ? 1 : -1;
                double f = Mth.sin(event.getSwingProgress() * event.getSwingProgress() * Mth.PI);
                double f1 = Mth.sin(Mth.sqrt(event.getSwingProgress()) * Mth.PI);
                PoseStack poseStack = event.getPoseStack();
                poseStack.translate(side * 0.56, -0.52 + event.getEquipProgress() * -0.6, -0.72);
                poseStack.translate(side * -0.1414214, 0.08, 0.1414214);
                poseStack.mulPose(Axis.XP.rotationDegrees((float) (-102.25F - f1 * 80.0F)));
                poseStack.mulPose(Axis.YP.rotationDegrees((float) (side * 13.365F - f * 20.0F)));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) (side * 78.050003F - f1 * 20.0F)));
                mc.getEntityRenderDispatcher()
                    .getItemInHandRenderer()
                    .renderItem(
                        player, item, flag ? 
                        ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, 
                        !flag, poseStack, event.getMultiBufferSource(), event.getPackedLight()
                    );
                event.setCanceled(true);
            }
        }
    }
}