package net.manaita_plus_neo.item.weapon;

import net.manaita_plus_neo.item.ManaitaWeaponBase;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;

public class ManaitaBow extends ManaitaWeaponBase {
    public ManaitaBow(Item.Properties properties) {
        super(properties, "bow");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            Arrow arrow = EntityType.ARROW.create(level);
            if (arrow != null) {
                arrow.setPos(player.getX(), player.getEyeY() - 0.1F, player.getZ());
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 10000.0F, 0.0F);
                arrow.setCritArrow(true);
                arrow.setBaseDamage(10000.0F);

                level.addFreshEntity(arrow);
            }
        }
        return InteractionResultHolder.success(itemstack);
    }
    
    @Override
    protected String getItemName() {
        return I18n.get("item.manaita_plus_neo.manaita_bow");
    }
}