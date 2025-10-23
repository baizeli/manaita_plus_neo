package net.manaita_plus_neo.event;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.item.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import java.util.Set;
import java.util.HashSet;

@EventBusSubscriber(modid = ManaitaPlusNeo.MOD_ID)
public class ManaitaItemEvents {

    private static Set<Item> manaitaItems = null;

    private static void initializeManaitaItems() {
        if (manaitaItems == null) {
            manaitaItems = new HashSet<>();
            
            // 砧板斧
            manaitaItems.add(ModItems.MANAITA_AXE.get());
            // 砧板镐
            manaitaItems.add(ModItems.MANAITA_PICKAXE.get());
            // 砧板铲
            manaitaItems.add(ModItems.MANAITA_SHOVEL.get());
            // 砧板锄
            manaitaItems.add(ModItems.MANAITA_HOE.get());
            // 砧板剪刀
            manaitaItems.add(ModItems.MANAITA_SHEARS.get());
            // 砧板弓
            manaitaItems.add(ModItems.MANAITA_BOW.get());
            // 砧板剑
            manaitaItems.add(ModItems.MANAITA_SWORD.get());
            // 砧板头盔
            manaitaItems.add(ModItems.MANAITA_HELMET.get());
            // 砧板胸甲
            manaitaItems.add(ModItems.MANAITA_CHESTPLATE.get());
            // 砧板护腿
            manaitaItems.add(ModItems.MANAITA_LEGGINGS.get());
            // 砧板靴子
            manaitaItems.add(ModItems.MANAITA_BOOTS.get());
            // 砧板多功能工具
            manaitaItems.add(ModItems.MANAITA_PAXEL.get());
            // 砧板之刃[神]
            manaitaItems.add(ModItems.MANAITA_SWORD_GOD.get());
            // 砧板工作台[类型]
            manaitaItems.add(ModItems.CRAFTING_BLOCK.get());
            // 砧板熔炉[类型]
            manaitaItems.add(ModItems.FURNACE_BLOCK.get());
            // 砧板钩[橡木]
            manaitaItems.add(ModItems.WOODEN_HOOK.get());
            // 砧板钩[类型]
            manaitaItems.add(ModItems.HOOK_BLOCK.get());
        }
    }

    private static boolean isManaitaItem(ItemStack stack) {
        initializeManaitaItems();
        return manaitaItems.contains(stack.getItem());
    }

    @SubscribeEvent
    public static void onItemEntitySpawn(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            if (isManaitaItem(stack)) {
                itemEntity.setUnlimitedLifetime();
                itemEntity.setInvulnerable(true);
            }
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        ItemStack stack = event.getEntity().getItem();
        if (isManaitaItem(stack)) {
            ItemEntity itemEntity = event.getEntity();
            itemEntity.setPickUpDelay(5);

            itemEntity.setDeltaMovement(
                itemEntity.getDeltaMovement().x * 0.3,
                itemEntity.getDeltaMovement().y * 0.5,
                itemEntity.getDeltaMovement().z * 0.3
            );
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack stack = itemEntity.getItem();
                return isManaitaItem(stack);
            }
            return false;
        });
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        for (ItemEntity itemEntity : event.getDrops()) {
            ItemStack stack = itemEntity.getItem();
            if (isManaitaItem(stack)) {
                itemEntity.setUnlimitedLifetime();
                itemEntity.setInvulnerable(true);
            }
        }
    }
}