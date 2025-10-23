package net.manaita_plus_neo.event;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.item.tools.ManaitaAxe;
import net.manaita_plus_neo.item.tools.ManaitaPickaxe;
import net.manaita_plus_neo.item.tools.ManaitaShovel;
import net.manaita_plus_neo.item.tools.ManaitaHoe;
import net.manaita_plus_neo.item.tools.ManaitaShears;
import net.manaita_plus_neo.item.tools.ManaitaPaxel;
import net.manaita_plus_neo.util.ManaitaToolUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import java.util.function.BiPredicate;

@EventBusSubscriber(modid = ManaitaPlusNeo.MOD_ID)
public class ManaitaToolEvents {
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemstack = player.getMainHandItem();

        if (itemstack.getItem() instanceof ManaitaAxe) {
            handleToolBreakEvent(event, itemstack, player, (tool, blockState) -> ((ManaitaAxe) tool.getItem()).isCorrectToolForDrops(tool, blockState));
        } else if (itemstack.getItem() instanceof ManaitaPickaxe) {
            handleToolBreakEvent(event, itemstack, player, (tool, blockState) -> ((ManaitaPickaxe) tool.getItem()).isCorrectToolForDrops(tool, blockState));
        } else if (itemstack.getItem() instanceof ManaitaShovel) {
            handleToolBreakEvent(event, itemstack, player, (tool, blockState) -> ((ManaitaShovel) tool.getItem()).isCorrectToolForDrops(tool, blockState));
        } else if (itemstack.getItem() instanceof ManaitaHoe) {
            handleToolBreakEvent(event, itemstack, player, (tool, blockState) -> ((ManaitaHoe) tool.getItem()).isCorrectToolForDrops(tool, blockState));
        } else if (itemstack.getItem() instanceof ManaitaShears) {
            handleToolBreakEvent(event, itemstack, player, (tool, blockState) -> ((ManaitaShears) tool.getItem()).isCorrectToolForDrops(tool, blockState));
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        Player player = event.getEntity();
        ItemStack itemstack = player.getMainHandItem();

        if (itemstack.getItem() instanceof ManaitaPaxel) {
            int range = ManaitaToolUtils.getRange(itemstack);

            ManaitaToolUtils.destroyBlocksInRange(
                itemstack,
                event.getLevel(),
                event.getPos(),
                player,
                range
            );
        }
    }

    private static void handleToolBreakEvent(BlockEvent.BreakEvent event, ItemStack itemstack, Player player, BiPredicate<ItemStack, BlockState> canMineBlock) {
        Level level = player.level();
        int range = ManaitaToolUtils.getRange(itemstack);

        ManaitaToolUtils.performRangeBreak(
            itemstack, 
            level, 
            event.getPos(), 
            player, 
            range, 
            canMineBlock
        );
    }
}