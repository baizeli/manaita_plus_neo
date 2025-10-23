package net.manaita_plus_neo.util;

import net.manaita_plus_neo.Config;
import net.manaita_plus_neo.item.tools.ManaitaPaxel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;
import java.util.function.BiPredicate;

import static net.minecraft.world.level.block.Block.popResource;

public class ManaitaRangeUtils {
    
    public static void performRangeBreak(
        ItemStack toolItem, 
        Level level, 
        BlockPos pos, 
        Player player, 
        int range, 
        BiPredicate<ItemStack, BlockState> canMineBlock
    ) {
        performRangeBreakWithHandler(toolItem, level, pos, player, range, canMineBlock, 
            getDefaultBlockHandler()
        );
    }
    
    public static void destroyBlocksInRange(
        ItemStack stack, 
        Level level, 
        BlockPos pos, 
        Player player, 
        int range
    ) {
        performRangeBreakWithHandler(stack, level, pos, player, range, 
            (tool, blockState) -> canDestroyBlock(blockState),
            getDefaultBlockHandler()
        );
    }
    
    private static void performRangeBreakWithHandler(
        ItemStack toolItem, 
        Level level, 
        BlockPos pos, 
        Player player, 
        int range, 
        BiPredicate<ItemStack, BlockState> canMineBlock,
        BlockHandler blockHandler
    ) {
        BlockState centerBlockState = level.getBlockState(pos);
        if (canMineBlock.test(toolItem, centerBlockState)) {
            blockHandler.handle(toolItem, level, player, pos, centerBlockState);
        }

        if (range > 1) {
            int radius = range / 2;
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x == 0 && y == 0 && z == 0) {
                            continue;
                        }

                        mutablePos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        BlockState blockstate = level.getBlockState(mutablePos);

                        if (canMineBlock.test(toolItem, blockstate)) {
                            blockHandler.handle(toolItem, level, player, mutablePos, blockstate);
                        }
                    }
                }
            }
        }
    }
    
    @FunctionalInterface
    private interface BlockHandler {
        void handle(ItemStack toolItem, Level level, Player player, BlockPos pos, BlockState blockstate);
    }
    
    private static BlockHandler getDefaultBlockHandler() {
        return (toolItem, lvl, ply, blockPos, blockstate) -> {
            BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(lvl, blockPos, blockstate, ply);
            handleDropsAndExp(breakEvent, toolItem);
            destroyBlockWithEnchantments(toolItem, lvl, ply, blockPos, blockstate);
        };
    }
    
    public static boolean canDestroyBlock(BlockState blockState) {
        return true;
    }
    
    private static void destroyBlockWithEnchantments(
        ItemStack toolItem, 
        Level level, 
        Player player, 
        BlockPos pos,
        BlockState blockstate
    ) {
        if (player.getAbilities().instabuild) {
            level.removeBlock(pos, false);
            return;
        }
        
        if (!(level instanceof ServerLevel)) {
            level.removeBlock(pos, false);
            return;
        }

        if (!ManaitaToolUtils.isDoublingEnabled(toolItem)) {
            Block.dropResources(blockstate, level, pos, null, player, toolItem);

            if (toolItem.getItem() instanceof ManaitaPaxel && blockstate.getBlock().defaultDestroyTime() < 0) {
                ItemStack bedrockDrop = new ItemStack(blockstate.getBlock());
                popResource(level, pos, bedrockDrop);
            }
        }
        
        level.removeBlock(pos, false);
    }
    
    public static void handleDropsAndExp(BlockEvent.BreakEvent event, ItemStack toolStack) {
        if (event.getPlayer().getAbilities().instabuild) {
            return;
        }
        
        if (ManaitaToolUtils.isDoublingEnabled(toolStack)) {
            Level level = event.getPlayer().level();
            BlockPos pos = event.getPos();

            BlockState state = event.getState();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, blockEntity, event.getPlayer(), toolStack);

            for (ItemStack drop : drops) {
                if (!drop.isEmpty()) {
                    ItemStack extraDrop = drop.copy();
                    extraDrop.setCount(extraDrop.getCount() * Config.destroy_doubling.get());
                    ItemEntity itemEntity = new ItemEntity(
                        level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, extraDrop
                    );
                    level.addFreshEntity(itemEntity);
                }
            }

            if (toolStack.getItem() instanceof ManaitaPaxel && state.getBlock().defaultDestroyTime() < 0) {
                ItemStack bedrockDrop = new ItemStack(state.getBlock());
                bedrockDrop.setCount(bedrockDrop.getCount() * Config.destroy_doubling.get());
                ItemEntity itemEntity = new ItemEntity(
                    level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, bedrockDrop
                );
                level.addFreshEntity(itemEntity);
            }

            if (!hasSilkTouch(EnchantmentHelper.getEnchantmentsForCrafting(toolStack))) {
                int exp = state.getExpDrop((ServerLevel) level, pos, blockEntity, event.getPlayer(), toolStack);
                if (exp > 0) {
                    ExperienceOrb.award((ServerLevel) level, Vec3.atCenterOf(pos), exp * Config.destroy_doubling.get());
                }
            }
        }
    }
    
    public static boolean hasSilkTouch(ItemEnchantments enchantments) {
        return enchantments.keySet().stream().anyMatch(holder -> holder.is(Enchantments.SILK_TOUCH));
    }
}