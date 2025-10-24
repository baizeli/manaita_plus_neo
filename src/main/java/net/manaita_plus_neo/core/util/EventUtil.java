package net.manaita_plus_neo.core.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.manaita_plus_neo.common.util.ManaitaPlusEntityList;
import net.manaita_plus_neo.common.util.ManaitaPlusUtils;
import net.manaita_plus_neo.item.weapon.ManaitaSwordGod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.EntityTickList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;

public class EventUtil {
    public static float getHealth(Player player) {
        if ((ManaitaPlusUtils.isManaita(player) || ManaitaPlusEntityList.manaita.accept(player))) {
            player.getEntityData().set(DATA_HEALTH_ID, 20.0F);
            return player.getMaxHealth();
        }
        if (ManaitaPlusEntityList.death.accept(player)) {
            return 0.0F;
        }
        return player.getEntityData().get(DATA_HEALTH_ID);
    }

    public static float getHealth(LivingEntity entity) {
//        ManaitaTransformationService.LOGGER.error(o);
        if (entity instanceof Player player && (ManaitaPlusUtils.isManaita(player) || ManaitaPlusEntityList.manaita.accept(player))) {
            entity.getEntityData().set(DATA_HEALTH_ID, player.getMaxHealth());
            return player.getMaxHealth();
        }
        if (ManaitaPlusEntityList.death.accept(entity)) {
            return 0.0F;
        }
        return entity.getEntityData().get(DATA_HEALTH_ID);
    }

    public static EntityTickList getTickingEntities(ClientLevel level) {
        ObjectIterator<Int2ObjectMap.Entry<Entity>> iterator = level.tickingEntities.active.int2ObjectEntrySet().iterator();
        Int2ObjectMap.Entry<Entity> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (ManaitaPlusEntityList.remove.accept(entry.getValue())) {
                iterator.remove();
            }
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (ManaitaPlusEntityList.manaita.accept(player) && !level.tickingEntities.contains(player)) {
            level.tickingEntities.add(player);
        }
        return level.tickingEntities;
    }

    public static EntityTickList getEntityTickList(ServerLevel level) {
        ObjectIterator<Int2ObjectMap.Entry<Entity>> iterator = level.entityTickList.active.int2ObjectEntrySet().iterator();
        Int2ObjectMap.Entry<Entity> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (ManaitaPlusEntityList.remove.accept(entry.getValue())) {
                iterator.remove();
            }
        }
        for (ServerPlayer player : level.players()) {
            if (ManaitaPlusEntityList.manaita.accept(player) && !level.entityTickList.contains(player)) {
                level.entityTickList.add(player);
            }
        }
        return level.entityTickList;
    }

    public static <T extends EntityAccess> Int2ObjectMap<T> getById(EntityLookup<T> lookup) {
        ObjectIterator<Int2ObjectMap.Entry<T>> iterator = lookup.byId.int2ObjectEntrySet().iterator();
        Int2ObjectMap.Entry<T> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue() instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity)) {
                iterator.remove();
            }
        }
        return lookup.byId;
    }

    public static double getAttributeValue(LivingEntity living, Holder<Attribute> attribute) {
        AttributeInstance attr = living.getAttribute(attribute);
        if (attr == null) return 0.0D;

        double value = attr.getValue(); // 直接访问属性值，避免无限递归

        if (attribute == Attributes.MAX_HEALTH && value < 20.0D) {
            attr.setBaseValue(20.0D);
            return 20.0D;
        } else if (attribute == Attributes.MOVEMENT_SPEED && value < 0.1D) {
            attr.setBaseValue(0.15D);
            return 0.15D;
        } else if (attribute == Attributes.FLYING_SPEED && value < 0.05D) {
            attr.setBaseValue(0.075D);
            return 0.075D;
        }

        return value;
    }

    public static boolean isManaita(LocalPlayer localPlayer) {
        return localPlayer.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && stack.getItem() instanceof ManaitaSwordGod) || ManaitaPlusEntityList.manaita.accept(localPlayer);
    }

    public static boolean isManaita(LivingEntity living) {
        return /*living instanceof Player player && ManaitaPlusUtils.isManaita(player) || */ManaitaPlusEntityList.manaita.accept(living);
    }

    public static boolean isManaitaSafe(LivingEntity living) {
        return ManaitaPlusEntityList.manaita.acceptSafe(living);
    }

    public static boolean isManaita(Entity entity) {
        return /*entity instanceof Player player && ManaitaPlusUtils.isManaita(player) ||*/ ManaitaPlusEntityList.manaita.accept(entity);
    }

    public static boolean isDead(LivingEntity living) {
        return ManaitaPlusEntityList.death.accept(living);
    }

    public static boolean isDead(LocalPlayer localPlayer) {
        return ManaitaPlusEntityList.death.accept(localPlayer);
    }

    public static boolean isDead(Entity entity) {
        return ManaitaPlusEntityList.death.accept(entity);
    }

    public static boolean isRemove(LivingEntity localPlayer) {
        return ManaitaPlusEntityList.remove.accept(localPlayer);
    }

    public static boolean isRemove(Entity entity) {
        return ManaitaPlusEntityList.remove.accept(entity);
    }

    public static float getMaxHealth(LivingEntity living) {
        float attributeValue = (float) living.getAttributeValue(Attributes.MAX_HEALTH);
        if (attributeValue < 20.0F) {
            living.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            return 20.0F;
        }

        return attributeValue;
    }

    public static void onFind(Map<?,?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getValue() instanceof List list1) {
                list1.removeIf(o -> o instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity));
            }
        }
    }

    public static void onIterator(List<Object> list1) {
        list1.removeIf(o -> o instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity));
    }

    public static void onIterator(Int2ObjectMap<Object> int2ObjectMap) {
        int2ObjectMap.int2ObjectEntrySet().removeIf(o -> o instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity));
    }

    public static boolean isNotSafe(@Nullable Screen screen) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (screen instanceof DeathScreen) {
                if (ManaitaPlusEntityList.manaita.accept(player))
                    return true;
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); ++i) {
                    ItemStack itemstack = inventory.getItem(i);
                    if (itemstack.getItem() instanceof ManaitaSwordGod)
                        return true;
                }
            }
        }
        return false;
    }
}
