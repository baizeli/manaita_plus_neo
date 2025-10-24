package net.manaita_plus_neo.item.weapon;

import net.manaita_plus_neo.common.util.ManaitaPlusUtils;
import net.manaita_plus_neo.entity.ManaitaPlusLightningBolt;
import net.manaita_plus_neo.entity.ModEntities;
import net.manaita_plus_neo.item.ManaitaWeaponBase;
import net.manaita_plus_neo.util.EntityUtils;
import net.manaita_plus_neo.util.ManaitaCommonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class ManaitaSwordGod extends ManaitaWeaponBase {
    
    public ManaitaSwordGod(Tier tier, Item.Properties properties) {
        super(properties, "sword_god");
    }
    
    @Override
    protected String getItemName() {
        return I18n.get("item.manaita_plus_neo.manaita_sword_god");
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ManaitaCommonUtils.addGodSwordTooltip(stack, tooltip);
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
            ClientLevel clientLevel = Minecraft.getInstance().level;
            EntityTickList newList = new EntityTickList();
            for (Entity e : clientLevel.entitiesForRendering()) {
                if (e != living) {
                    newList.add(e);
                }
            }
            clientLevel.tickingEntities = newList;
            TransientEntitySectionManager<Entity> manager = clientLevel.entityStorage;
            for (Entity e : manager.entityStorage.getAllEntities()) {
                if (e != null) {
                    if (e == entity) {
                        manager.entityStorage.remove(e);
                    }
                }
            }
            clientLevel.entityStorage = manager;
        }
        ManaitaPlusUtils.attack(entity, player, true);
        return super.onLeftClickEntity(stack, player, entity);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        
        if (!level.isClientSide) {
            Vec3 position = player.position();
            Random random = new Random();
            
            for (int i = 0; i < 100; i++) {
                ManaitaPlusLightningBolt bolt = ModEntities.MANAITA_LIGHTNING_BOLT.get().create(level);
                if (bolt != null) {
                    double angle = random.nextDouble() * 20.0D * Math.PI;
                    double distance = random.nextGaussian() * 100.0D + 3;
                    double x = Math.sin(angle) * distance + position.x;
                    double z = Math.cos(angle) * distance + position.z;

                    int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, (int) x, (int) z);
                    bolt.setPos(x, y, z);
                    level.addFreshEntity(bolt);
                }
            }
        }

        ManaitaPlusUtils.godKill(player, true, !player.isShiftKeyDown());

        return InteractionResultHolder.pass(itemstack);
    }
    
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}