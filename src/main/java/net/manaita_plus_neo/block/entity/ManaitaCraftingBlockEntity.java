package net.manaita_plus_neo.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ManaitaCraftingBlockEntity extends BlockEntity {
    public ManaitaCraftingBlockEntity(BlockPos pos, BlockState state) {
        // 砧板工作台
        super(ModBlockEntities.CRAFTING_BLOCK_ENTITY.get(), pos, state);
    }
}