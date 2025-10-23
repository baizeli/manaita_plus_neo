package net.manaita_plus_neo.block.entity;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ManaitaPlusNeo.MOD_ID);
    
    // 砧板工作台
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaitaCraftingBlockEntity>> CRAFTING_BLOCK_ENTITY = 
        BLOCK_ENTITIES.register("crafting_block_entity", () -> 
            BlockEntityType.Builder.of(ManaitaCraftingBlockEntity::new, ModBlocks.CRAFTING_BLOCK.get()).build(null)
        );

    // 砧板熔炉
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaitaFurnaceBlockEntity>> FURNACE_BLOCK_ENTITY = 
        BLOCK_ENTITIES.register("furnace_block_entity", () -> 
            BlockEntityType.Builder.of(ManaitaFurnaceBlockEntity::new, ModBlocks.FURNACE_BLOCK.get()).build(null)
        );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}