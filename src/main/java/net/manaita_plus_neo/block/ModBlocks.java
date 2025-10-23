package net.manaita_plus_neo.block;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ManaitaPlusNeo.MOD_ID);
    
    // 砧板工作台
    public static final DeferredBlock<ManaitaCraftingBlock> CRAFTING_BLOCK = BLOCKS.register("block_crafting_manaita", 
        () -> new ManaitaCraftingBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.5F)
            .sound(SoundType.WOOD)
            .forceSolidOff()
        )
    );
    
    // 砧板熔炉
    public static final DeferredBlock<ManaitaFurnaceBlock> FURNACE_BLOCK = BLOCKS.register("block_furnace_manaita", 
        () -> new ManaitaFurnaceBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.5F)
            .sound(SoundType.WOOD)
            .forceSolidOff()
        )
    );
    
    // 砧板钩
    public static final DeferredBlock<ManaitaHookBlock> HOOK_BLOCK = BLOCKS.register("block_hook_manaita", 
        () -> new ManaitaHookBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.5F)
            .sound(SoundType.WOOD)
            .forceSolidOff()
        )
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}