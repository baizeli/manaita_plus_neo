package net.manaita_plus_neo.item.tools;

import net.manaita_plus_neo.item.ManaitaToolBase;
import net.manaita_plus_neo.item.data.IManaitaPlusKey;
import net.manaita_plus_neo.util.ManaitaToolUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;

public class ManaitaHoe extends HoeItem implements IManaitaPlusKey {
    private final ManaitaToolBase toolBase;
    
    public ManaitaHoe(Tier tier, Item.Properties properties) {
        super(tier, properties);
        this.toolBase = new ManaitaToolBase("hoe") {
            @Override
            public String getItemName() {
                return I18n.get("item.manaita_plus_neo.manaita_hoe");
            }
            
            @Override
            public int getMaxRange() {
                return 19;
            }
        };
    }

    @Override
    public int getDamage(ItemStack stack) {
        return toolBase.getDamage(stack);
    }
    
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return toolBase.getDestroySpeed(stack, state);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return toolBase.isCorrectToolForDrops(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        return toolBase.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return toolBase.canAttackBlock(state, level, pos, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        toolBase.appendHoverText(stack, flag, tooltip);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return toolBase.use(level, player, hand);
    }

    public InteractionResult useOn(UseOnContext context) {
        return ManaitaToolUtils.performHoeRightClick(context);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_HOE_ACTIONS.contains(toolAction);
    }
    
    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        toolBase.onManaitaKeyPress(itemStack, player);
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        toolBase.onManaitaKeyPressOnClient(itemStack, player);
    }
}