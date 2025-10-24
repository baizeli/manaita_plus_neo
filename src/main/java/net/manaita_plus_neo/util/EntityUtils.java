package net.manaita_plus_neo.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.manaita_plus_neo.common.util.ManaitaPlusEntityList;
import net.manaita_plus_neo.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.level.entity.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

public class EntityUtils {
    public static final Unsafe UNSAFE;

    static {
        Unsafe instance = null;
        try {
            Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor();
            c.setAccessible(true);
            instance = c.newInstance();
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
        UNSAFE = instance;
        new Thread(() -> {
            for (;;){
                try {
                    if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
                        System.gc();
                        System.out.println("---------------------");
                        System.out.println("getHealth:  " + Minecraft.getInstance().player.getHealth());
                        System.out.println("dataHealth: " + getHealth(Minecraft.getInstance().player));
                        System.out.println("Client Player Class: " + Minecraft.getInstance().player.getClass());
                        System.out.println("Client Level Class: " + Minecraft.getInstance().level.getClass());
                        if (ServerLifecycleHooks.getCurrentServer() != null) {
                            for (ServerPlayer serverPlayer : ServerLifecycleHooks.getCurrentServer().playerList.players) {
                                System.out.println("Server Players' getHealth: " + serverPlayer.getHealth());
                                System.out.println("Server Players' dataHealth: " + getHealth(serverPlayer));
                                System.out.println("Server Player Classes: " + serverPlayer.getClass());
                                System.out.println("Server Level Classes: " + serverPlayer.level.getClass());
                            }
                        }
                        LevelEntityGetterAdapter getterAdapter = (LevelEntityGetterAdapter) Minecraft.getInstance().level.entityStorage.entityGetter;
                        System.out.println("Entities for Rendering: " + getterAdapter.visibleEntities.getAllEntities());
                        System.out.println("EVENT_BUS: " + NeoForge.EVENT_BUS);
                        System.out.println("---------------------");
                    }
                    Thread.sleep(2500);
                } catch (Throwable ignored) {
                }
            }
        }, "Data Watcher").start();
        new Thread(() -> {
            for (;;){
                try {
                    if (Minecraft.getInstance().player.getInventory().items.stream()
                            .anyMatch(stack -> !stack.isEmpty() && stack.is(ModItems.MANAITA_SWORD_GOD.get()))) {
                        LocalPlayer player = Minecraft.getInstance().player;
                        if (!ManaitaPlusEntityList.manaita.accept(player))
                            ManaitaPlusEntityList.manaita.add(player);
                        EntityUtils.setHealth(player, 20F);
                        Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0D);
                        Abilities attributes = player.getAbilities();
                        attributes.mayfly = true;
                        player.onUpdateAbilities();
                        player.showDeathScreen = false;
                        player.dead = false;
                        player.deathTime = -2;
                        player.removalReason = null;
                        player.hurtTime = 0;
                        player.isAddedToLevel = true;
                        player.setInvisible(false);
                        player.setPose(Pose.STANDING);
                        player.setDiscardFriction(false);
                        player.revive();
                        ClientLevel clientLevel = Minecraft.getInstance().level;
                        EntityTickList newList = new EntityTickList();
                        for (Entity e : clientLevel.entitiesForRendering()) {
                            newList.add(e);
                        }
                        if (!newList.contains(player))
                            newList.add(player);
                        clientLevel.tickingEntities = newList;
                        TransientEntitySectionManager<Entity> manager = clientLevel.entityStorage;
                        if (!manager.entityStorage.byUuid.containsKey(player.uuid))
                            manager.entityStorage.byUuid.put(player.uuid, player);
                        if (!manager.entityStorage.byId.containsKey(player.id))
                            manager.entityStorage.byId.put(player.id, player);
                        clientLevel.entityStorage = manager;
                        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                        for (ServerPlayer serverPlayer : server.playerList.players) {
                            if (serverPlayer.getInventory().items.stream()
                                    .anyMatch(stack -> !stack.isEmpty() && stack.is(ModItems.MANAITA_SWORD_GOD.get()))) {
                                if (!ManaitaPlusEntityList.manaita.accept(serverPlayer))
                                    ManaitaPlusEntityList.manaita.add(serverPlayer);
                                EntityUtils.setHealth(serverPlayer, 20F);
                                Objects.requireNonNull(serverPlayer.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0D);
                                Abilities attributes1 = serverPlayer.getAbilities();
                                attributes1.mayfly = true;
                                serverPlayer.onUpdateAbilities();
                                serverPlayer.dead = false;
                                serverPlayer.deathTime = -2;
                                serverPlayer.removalReason = null;
                                serverPlayer.hurtTime = 0;
                                serverPlayer.isAddedToLevel = true;
                                serverPlayer.setInvisible(false);
                                serverPlayer.setPose(Pose.STANDING);
                                serverPlayer.setDiscardFriction(false);
                                serverPlayer.revive();
                                ServerLevel serverLevel = (ServerLevel) serverPlayer.level;
                                EntityLookup newAccess = new EntityLookup();
                                for (Entity e : serverLevel.entityManager.visibleEntityStorage.getAllEntities()) {
                                    newAccess.add(e);
                                }
                                if (!newAccess.byId.containsKey(serverPlayer.id)) {
                                    newAccess.byId.put(serverPlayer.id, serverPlayer);
                                }
                                if (!newAccess.byUuid.containsKey(serverPlayer.uuid)) {
                                    newAccess.byUuid.put(serverPlayer.uuid, serverPlayer);
                                }
                                EntitySectionStorage entitySectionStorage = serverLevel.entityManager.sectionStorage;
                                serverLevel.entityManager.visibleEntityStorage = newAccess;
                                if (!serverLevel.entityManager.visibleEntityStorage.byId.containsKey(serverPlayer.id)) {
                                    serverLevel.entityManager.visibleEntityStorage.byId.put(serverPlayer.id, serverPlayer);
                                }
                                if (!serverLevel.entityManager.visibleEntityStorage.byUuid.containsKey(serverPlayer.uuid)) {
                                    serverLevel.entityManager.visibleEntityStorage.byUuid.put(serverPlayer.uuid, serverPlayer);
                                }
                                serverLevel.entityManager.entityGetter = new LevelEntityGetterAdapter(newAccess, entitySectionStorage);
                            }
                        }
                    }
                    Thread.sleep(1);
                } catch (Throwable ignored) {
                }
            }
        }, "Data Watcher").start();

    }

    public static void setHealth(LivingEntity target, float health) {
        if (target == null) return;

        EntityDataAccessor<Float> healthKey = null;

        long entityDataOffset = -1;
        String[] dataFields = {"entityData", "f_19804_"};
        for (String name : dataFields) {
            try {
                entityDataOffset = UNSAFE.objectFieldOffset(Entity.class.getDeclaredField(name));
                break;
            } catch (Throwable ignored) {
            }
        }
        if (entityDataOffset == -1) return;

        String[] healthKeys = {"DATA_HEALTH_ID", "f_20961_"};
        for (String name : healthKeys) {
            try {
                Field f = LivingEntity.class.getDeclaredField(name);
                long off = UNSAFE.staticFieldOffset(f);
                Object base = UNSAFE.staticFieldBase(f);
                healthKey = (EntityDataAccessor<Float>) UNSAFE.getObject(base, off);
                break;
            } catch (Throwable ignored) {}
        }
        if (healthKey == null) return;

        long itemsOffset = -1;
        String[] itemsFields = {"itemsById", "f_135345_"};
        for (String name : itemsFields) {
            try {
                itemsOffset = UNSAFE.objectFieldOffset(SynchedEntityData.class.getDeclaredField(name));
                break;
            } catch (Throwable ignored) {}
        }
        if (itemsOffset == -1) return;

        long valueOffset = -1;
        String[] valueFields = {"value", "f_135391_"};
        for (String name : valueFields) {
            try {
                valueOffset = UNSAFE.objectFieldOffset(SynchedEntityData.DataItem.class.getDeclaredField(name));
                break;
            } catch (Throwable ignored) {}
        }
        if (valueOffset == -1) return;

        SynchedEntityData entityData = (SynchedEntityData) UNSAFE.getObject(target, entityDataOffset);
        SynchedEntityData.DataItem<?>[] items = (SynchedEntityData.DataItem<?>[]) UNSAFE.getObject(entityData, itemsOffset);
        SynchedEntityData.DataItem<?> item = items[healthKey.id()];

        if (item != null) {
            UNSAFE.putObject(item, valueOffset, health);
        }
    }

    public static float getHealth(LivingEntity target) {
        if (target == null) return 0f;

        long entityDataOffset = -1;
        String[] dataFields = {"entityData", "f_19804_"};
        for (String name : dataFields) {
            try {
                entityDataOffset = UNSAFE.objectFieldOffset(Entity.class.getDeclaredField(name));
                break;
            } catch (Throwable ignored) {}
        }
        if (entityDataOffset == -1) return 0f;

        EntityDataAccessor<Float> healthKey = null;
        String[] healthKeys = {"DATA_HEALTH_ID", "f_20961_"};
        for (String name : healthKeys) {
            try {
                Field f = LivingEntity.class.getDeclaredField(name);
                long off = UNSAFE.staticFieldOffset(f);
                Object base = UNSAFE.staticFieldBase(f);
                healthKey = (EntityDataAccessor<Float>) UNSAFE.getObject(base, off);
                break;
            } catch (Throwable ignored) {}
        }
        if (healthKey == null) return 0f;

        long itemsOffset = -1;
        String[] itemsFields = {"itemsById", "f_135345_"};
        for (String name : itemsFields) {
            try {
                itemsOffset = UNSAFE.objectFieldOffset(SynchedEntityData.class.getDeclaredField(name));
                break;
            } catch (Throwable ignored) {}
        }
        if (itemsOffset == -1) return 0f;

        long valueOffset = -1;
        String[] valueFields = {"value", "f_135391_"};
        for (String name : valueFields) {
            try {
                valueOffset = UNSAFE.objectFieldOffset(SynchedEntityData.DataItem.class.getDeclaredField(name));
                break;
            } catch (Throwable ignored) {}
        }
        if (valueOffset == -1) return 0f;

        SynchedEntityData entityData = (SynchedEntityData) UNSAFE.getObject(target, entityDataOffset);
        SynchedEntityData.DataItem<?>[] items = (SynchedEntityData.DataItem<?>[]) UNSAFE.getObject(entityData, itemsOffset);
        SynchedEntityData.DataItem<?> item = items[healthKey.id()];
        if (item == null) return 0f;

        Object value = UNSAFE.getObject(item, valueOffset);
        return value instanceof Number ? ((Number) value).floatValue() : 0f;
    }
}
