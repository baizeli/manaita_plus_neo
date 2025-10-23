package net.manaita_plus_neo.block;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.manaita_plus_neo.block.data.Data;
import net.manaita_plus_neo.block.entity.ManaitaCraftingBlockEntity;
import net.manaita_plus_neo.common.menu.ManaitaCraftingMenu;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class ManaitaCraftingBlock extends BaseEntityBlock {

    public static final MapCodec<ManaitaCraftingBlock> CODEC = simpleCodec(ManaitaCraftingBlock::new);

    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting_manaita");

    public ManaitaCraftingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(Data.HOOK, 8).setValue(Data.FACING, Direction.NORTH).setValue(Data.WALL, Direction.DOWN).setValue(Data.TYPES, 0));
    }

    @Override
    protected MapCodec<ManaitaCraftingBlock> codec() {
        return CODEC;
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return ItemInteractionResult.CONSUME;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
            (containerId, playerInventory, player) ->
            new ManaitaCraftingMenu(containerId, playerInventory, ContainerLevelAccess.create(level, pos)), 
            CONTAINER_TITLE
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(Data.WALL, context.getClickedFace().getOpposite()).setValue(Data.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(Data.FACING, rotation.rotate(state.getValue(Data.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(Data.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(Data.FACING, Data.TYPES, Data.WALL, Data.HOOK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaitaCraftingBlockEntity(pos, state);
    }
}