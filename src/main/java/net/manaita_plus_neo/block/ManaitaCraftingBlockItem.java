package net.manaita_plus_neo.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.manaita_plus_neo.block.data.Data;

public class ManaitaCraftingBlockItem extends BlockItem {
    public ManaitaCraftingBlockItem(ManaitaCraftingBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        int type = 0;
        var customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            var tag = customData.getUnsafe();
            if (tag.contains("ManaitaType")) {
                type = tag.getInt("ManaitaType");
            }
        }
        return Component.literal(I18n.get("block.crafting." + getTypeName(type) + "name"));
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (!this.getBlock().isEnabled(context.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL;
        } else if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(context);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    
                    if (!level.setBlock(blockpos, blockstate, 11)) {
                        return InteractionResult.FAIL;
                    } else {
                        BlockState blockstate1 = level.getBlockState(blockpos);

                        if (blockstate1.is(blockstate.getBlock())) {
                            blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                            this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                            blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                            if (player instanceof ServerPlayer) {
                                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                            }
                        }

                        SoundType soundtype = blockstate1.getSoundType(level, blockpos, context.getPlayer());
                        level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, context.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                        if (player == null || !player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }

                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
        BlockState blockstate = state;
        if (blockstate.getBlock() instanceof ManaitaCraftingBlock) {
            var customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                var tag = customData.getUnsafe();
                if (tag.contains("ManaitaType")) {
                    int typeValue = tag.getInt("ManaitaType");
                    blockstate = blockstate.setValue(Data.TYPES, typeValue);
                    level.setBlock(pos, blockstate, 2);
                    return blockstate;
                }
            }
        }

        return blockstate;
    }

    private String getTypeName(int type) {
        switch (type) {
            case 0: return "";
            case 1: return "wooden.";
            case 2: return "stone.";
            case 3: return "iron.";
            case 4: return "gold.";
            case 5: return "diamond.";
            case 6: return "emerald.";
            case 7: return "redstone.";
            case 8: return "netherite.";
            default: return "";
        }
    }
}