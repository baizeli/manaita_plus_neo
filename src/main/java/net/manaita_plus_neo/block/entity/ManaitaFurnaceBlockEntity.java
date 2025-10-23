package net.manaita_plus_neo.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.manaita_plus_neo.Config;
import net.manaita_plus_neo.common.menu.ManaitaFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class ManaitaFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    
    public ManaitaFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FURNACE_BLOCK_ENTITY.get(), pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.furnace_manaita");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ManaitaFurnaceMenu(containerId, inventory, this, this.dataAccess);
    }

    protected void createExperience(ServerLevel level, Vec3 pos, int recipeIndex, float experience) {
        int i = Mth.floor((float)recipeIndex * experience * Config.furnace_doubling.get());
        float f = Mth.frac((float)recipeIndex * experience * Config.furnace_doubling.get());
        if (f != 0.0F && Math.random() < (double)f) {
            i++;
        }

        ExperienceOrb.award(level, pos, i);
    }

    @Override
    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 popVec) {
        List<RecipeHolder<?>> list = Lists.newArrayList();
        try {
            Field recipesUsedField = AbstractFurnaceBlockEntity.class.getDeclaredField("recipesUsed");
            recipesUsedField.setAccessible(true);
            Object2IntOpenHashMap<ResourceLocation> recipesUsedMap = 
                (Object2IntOpenHashMap<ResourceLocation>) recipesUsedField.get(this);
            
            for (Object2IntMap.Entry<ResourceLocation> entry : recipesUsedMap.object2IntEntrySet()) {
                level.getRecipeManager().byKey(entry.getKey()).ifPresent(p_300839_ -> {
                    list.add((RecipeHolder<?>)p_300839_);
                    createExperience(level, popVec, entry.getIntValue(), ((AbstractCookingRecipe)p_300839_.value()).getExperience());
                });
            }
        } catch (Exception e) {
            return super.getRecipesToAwardAndPopExperience(level, popVec);
        }

        return list;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ManaitaFurnaceBlockEntity blockEntity) {
        boolean flag1 = false;
        ItemStack itemstack = blockEntity.items.get(0);

        if (!itemstack.isEmpty()) {
            Optional<RecipeHolder<? extends AbstractCookingRecipe>> recipeOptional = blockEntity.getSmeltingRecipeFor(itemstack, level);
            
            if (recipeOptional.isPresent()) {
                RecipeHolder<? extends AbstractCookingRecipe> recipe = recipeOptional.get();

                int burnCount = 0;
                while (blockEntity.canBurn(level.registryAccess(), recipe)) {
                    if (blockEntity.burn(recipe)) {
                        burnCount++;
                        flag1 = true;
                    } else {
                        break;
                    }
                }

                for (int i = 0; i < burnCount; i++) {
                    blockEntity.setRecipeUsed(recipe);
                }
            }
        }

        if (flag1) {
            setChanged(level, pos, state);
        }
    }

    private Optional<RecipeHolder<? extends AbstractCookingRecipe>> getSmeltingRecipeFor(ItemStack itemStack, Level level) {
        Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(itemStack), level);
        return recipe.map(r -> (RecipeHolder<? extends AbstractCookingRecipe>) r);
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        return 0;
    }

    protected boolean canBurn(HolderLookup.Provider registryAccess, @Nullable RecipeHolder<? extends AbstractCookingRecipe> recipe) {
        if (!this.items.get(0).isEmpty() && recipe != null) {
            ItemStack itemstack = recipe.value().assemble(new SingleRecipeInput(this.getItem(0)), registryAccess);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.items.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItemSameComponents(itemstack1, itemstack)) {
                    return false;
                } else {
                    int resultCount = itemstack.getCount() * Config.furnace_doubling.get();
                    return itemstack1.getCount() + resultCount <= this.getMaxStackSize() && 
                    itemstack1.getCount() + resultCount <= itemstack1.getMaxStackSize()
                    ? true
                    : itemstack1.getCount() + resultCount <= itemstack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    protected boolean burn(@Nullable RecipeHolder<? extends AbstractCookingRecipe> recipe) {
        if (recipe != null && this.canBurn(this.level.registryAccess(), recipe)) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = recipe.value().assemble(new SingleRecipeInput(this.getItem(0)), this.level.registryAccess());
            ItemStack itemstack2 = this.items.get(2);
            if (itemstack2.isEmpty()) {
                ItemStack copy = itemstack1.copy();
                copy.setCount(copy.getCount() * Config.furnace_doubling.get());
                this.items.set(2, copy);
            } else if (ItemStack.isSameItemSameComponents(itemstack2, itemstack1)) {
                itemstack2.grow(itemstack1.getCount() * Config.furnace_doubling.get());
            }

            if (itemstack.is(Blocks.WET_SPONGE.asItem()) && !this.items.get(1).isEmpty() && this.items.get(1).is(Items.BUCKET)) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }
}