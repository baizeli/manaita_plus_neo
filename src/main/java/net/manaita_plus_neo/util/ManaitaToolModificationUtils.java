package net.manaita_plus_neo.util;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.Optional;

public class ManaitaToolModificationUtils {
    
    public static InteractionResult performAxeRightClick(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        int i = ManaitaToolUtils.getRange(context.getItemInHand()) >> 1;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int xM = blockpos.getX() + i;
        int yM = blockpos.getY() + i;
        int zM = blockpos.getZ() + i;
        boolean flag = false;
        for (int x = blockpos.getX() - i; x <= xM; x++) {
            for (int y = blockpos.getY() - i; y <= yM; y++) {
                for (int z = blockpos.getZ() - i; z <= zM; z++) {
                    mutableBlockPos.set(x, y, z);
                    BlockState blockstate = level.getBlockState(mutableBlockPos);

                    Optional<BlockState> optional = Optional.ofNullable(
                        blockstate.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false)
                    );

                    Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : 
                    Optional.ofNullable(
                        blockstate.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, false)
                    );

                    Optional<BlockState> optional2 = optional.isPresent() || optional1.isPresent() ? Optional.empty() : 
                    Optional.ofNullable(
                        blockstate.getToolModifiedState(context, ItemAbilities.AXE_WAX_OFF, false)
                    );

                    Optional<BlockState> optional3 = Optional.empty();

                    if (optional.isPresent()) {
                        level.playSound(player, mutableBlockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                        optional3 = optional;
                    } else if (optional1.isPresent()) {
                        level.playSound(player, mutableBlockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.levelEvent(player, 3005, mutableBlockPos, 0);
                        optional3 = optional1;
                    } else if (optional2.isPresent()) {
                        level.playSound(player, mutableBlockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.levelEvent(player, 3004, mutableBlockPos, 0);
                        optional3 = optional2;
                    }

                    if (optional3.isPresent()) {
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, mutableBlockPos, context.getItemInHand());
                        }

                        level.setBlock(mutableBlockPos, optional3.get(), 11);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, mutableBlockPos, GameEvent.Context.of(player, optional3.get()));
                        if (player != null) {
                            context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                        }
                        flag = true;
                    }
                }
            }
        }
        return flag ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }
    
    public static InteractionResult performHoeRightClick(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        int i = ManaitaToolUtils.getRange(context.getItemInHand()) >> 1;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int xM = blockpos.getX() + i;
        int yM = blockpos.getY() + i;
        int zM = blockpos.getZ() + i;
        boolean flag = false;
        for (int x = blockpos.getX() - i; x <= xM; x++) {
            for (int y = blockpos.getY() - i; y <= yM; y++) {
                for (int z = blockpos.getZ() - i; z <= zM; z++) {
                    mutableBlockPos.set(x, y, z);
                    BlockState toolModifiedState = level.getBlockState(mutableBlockPos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false);
                    if (toolModifiedState != null) {
                        level.playSound(player, mutableBlockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!level.isClientSide) {
                            level.setBlock(mutableBlockPos, toolModifiedState, 11);
                            level.gameEvent(GameEvent.BLOCK_CHANGE, mutableBlockPos, GameEvent.Context.of(player, toolModifiedState));
                            if (player != null) {
                                context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                            }
                        }
                        flag = true;
                    }
                }
            }
        }
        return flag ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }
    
    public static InteractionResult performShovelRightClick(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        int i = ManaitaToolUtils.getRange(context.getItemInHand()) >> 1;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int xM = blockpos.getX() + i;
        int yM = blockpos.getY() + i;
        int zM = blockpos.getZ() + i;
        boolean flag = false;
        
        for (int x = blockpos.getX() - i; x <= xM; x++) {
            for (int y = blockpos.getY() - i; y <= yM; y++) {
                for (int z = blockpos.getZ() - i; z <= zM; z++) {
                    mutableBlockPos.set(x, y, z);
                    BlockState blockstate = level.getBlockState(mutableBlockPos);

                    if (context.getClickedFace() != Direction.DOWN)  {
                        BlockState shovelModifiedState = blockstate.getToolModifiedState(context, ItemAbilities.SHOVEL_FLATTEN, false);
                        BlockState blockstate2 = null;
                        if (shovelModifiedState != null && level.isEmptyBlock(mutableBlockPos.above())) {
                            level.playSound(player, mutableBlockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                            blockstate2 = shovelModifiedState;
                        } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                            if (!level.isClientSide()) {
                                level.levelEvent(null, 1009, mutableBlockPos, 0);
                            }

                            CampfireBlock.dowse(context.getPlayer(), level, mutableBlockPos, blockstate);
                            blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.FALSE);
                        }

                        if (blockstate2 != null) {
                            if (!level.isClientSide) {
                                level.setBlock(mutableBlockPos, blockstate2, 11);
                                level.gameEvent(GameEvent.BLOCK_CHANGE, mutableBlockPos, GameEvent.Context.of(player, blockstate2));
                                if (player != null) {
                                    context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                                }
                            }

                            flag = true;
                        }
                    }
                }
            }
        }
        return flag ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }
}