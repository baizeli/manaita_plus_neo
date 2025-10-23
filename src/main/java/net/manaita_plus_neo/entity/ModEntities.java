package net.manaita_plus_neo.entity;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, ManaitaPlusNeo.MOD_ID);
    
    // 砧板彩色闪电
    public static final DeferredHolder<EntityType<?>, EntityType<ManaitaPlusLightningBolt>> MANAITA_LIGHTNING_BOLT = 
        ENTITY_TYPES.register("manaita_lightning_bolt", () -> 
            EntityType.Builder.of(ManaitaPlusLightningBolt::new, MobCategory.MISC)
                .noSave()
                .sized(0.0F, 0.0F)
                .clientTrackingRange(16)
                .updateInterval(Integer.MAX_VALUE)
                .build("manaita_lightning_bolt")
        );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}