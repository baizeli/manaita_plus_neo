package net.manaita_plus_neo.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.manaita_plus_neo.block.data.Data;
import net.manaita_plus_neo.block.entity.ManaitaFurnaceBlockEntity;
import net.manaita_plus_neo.block.entity.ModBlockEntities;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;
import com.mojang.serialization.MapCodec;

import java.util.List;

public class ManaitaFurnaceBlock extends AbstractFurnaceBlock {
    public static final MapCodec<ManaitaFurnaceBlock> CODEC = simpleCodec(ManaitaFurnaceBlock::new);

    public ManaitaFurnaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(Data.HOOK, 8)
            .setValue(FACING, Direction.NORTH)
            .setValue(Data.WALL, Direction.DOWN)
            .setValue(LIT, Boolean.FALSE)
            .setValue(Data.TYPES, 0));
    }

    @Override
    protected MapCodec<ManaitaFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof ManaitaFurnaceBlockEntity) {
            player.openMenu((MenuProvider) blockentity);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> list = Lists.newArrayList();
        ItemStack itemStack = new ItemStack(state.getBlock());

        CompoundTag tag = new CompoundTag();
        tag.putInt("ManaitaType", state.getValue(Data.TYPES));
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        list.add(itemStack);
        int hook = state.getValue(Data.HOOK);
        if (hook != 8) {}
        return list;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(FACING, context.getHorizontalDirection().getOpposite())
            .setValue(Data.WALL, context.getClickedFace().getOpposite())
            .setValue(Data.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction wall = state.getValue(Data.WALL);
        Direction direction = state.getValue(Data.FACING);
        return wall == Direction.EAST ? Data.shapeEAST : 
               wall == Direction.SOUTH ? Data.shapeSOUTH : 
               wall == Direction.NORTH ? Data.shapeNORTH : 
               wall == Direction.WEST ? Data.shapeWEST : 
               direction == Direction.NORTH || 
               direction == Direction.SOUTH ? 
               wall == Direction.UP ? Data.shapeUNS : Data.shapeDNS : 
               wall == Direction.UP ? Data.shapeUWE : Data.shapeDWE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaitaFurnaceBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, Data.TYPES, Data.WALL, Data.HOOK);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.FURNACE_BLOCK_ENTITY.get(), ManaitaFurnaceBlockEntity::serverTick);
    }
}