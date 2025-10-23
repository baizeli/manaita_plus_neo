package net.manaita_plus_neo;

import net.manaita_plus_neo.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.nbt.CompoundTag;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ManaitaPlusNeo.MOD_ID);

    // 更好的砧板
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MANAITA_PLUS_TAB = CREATIVE_TABS.register("manaita_plus_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.ManaitaPlusTab"))
            .icon(() -> new ItemStack(ModItems.CRAFTING_BLOCK.get()))
            .displayItems((parameters, output) -> {
                // 砧板工作台
                ItemStack basicCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                output.accept(basicCraftingBlock);
                
                // 木制砧板工作台
                ItemStack woodenCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                CompoundTag woodenTag = new CompoundTag();
                woodenTag.putInt("ManaitaType", 1);
                woodenCraftingBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(woodenTag));
                output.accept(woodenCraftingBlock);
                
                // 石制砧板工作台
                ItemStack stoneCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                CompoundTag stoneTag = new CompoundTag();
                stoneTag.putInt("ManaitaType", 2);
                stoneCraftingBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(stoneTag));
                output.accept(stoneCraftingBlock);
                
                // 铁制砧板工作台
                ItemStack ironCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                CompoundTag ironTag = new CompoundTag();
                ironTag.putInt("ManaitaType", 3);
                ironCraftingBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(ironTag));
                output.accept(ironCraftingBlock);
                
                // 金制砧板工作台
                ItemStack goldCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                CompoundTag goldTag = new CompoundTag();
                goldTag.putInt("ManaitaType", 4);
                goldCraftingBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(goldTag));
                output.accept(goldCraftingBlock);
                
                // 钻石制砧板工作台
                ItemStack diamondCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                CompoundTag diamondTag = new CompoundTag();
                diamondTag.putInt("ManaitaType", 5);
                diamondCraftingBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(diamondTag));
                output.accept(diamondCraftingBlock);
                
                // 绿宝石制砧板工作台
                ItemStack emeraldCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                CompoundTag emeraldTag = new CompoundTag();
                emeraldTag.putInt("ManaitaType", 6);
                emeraldCraftingBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(emeraldTag));
                output.accept(emeraldCraftingBlock);
                
                // 红石制砧板工作台
                ItemStack redstoneCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                CompoundTag redstoneTag = new CompoundTag();
                redstoneTag.putInt("ManaitaType", 7);
                redstoneCraftingBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(redstoneTag));
                output.accept(redstoneCraftingBlock);
                
                // 下界合金制砧板工作台
                ItemStack netheriteCraftingBlock = new ItemStack(ModItems.CRAFTING_BLOCK.get());
                CompoundTag netheriteTag = new CompoundTag();
                netheriteTag.putInt("ManaitaType", 8);
                netheriteCraftingBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(netheriteTag));
                output.accept(netheriteCraftingBlock);

                // 砧板熔炉
                ItemStack basicFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                output.accept(basicFurnaceBlock);
                
                // 木制砧板熔炉
                ItemStack woodenFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                CompoundTag woodenFurnaceTag = new CompoundTag();
                woodenFurnaceTag.putInt("ManaitaType", 1);
                woodenFurnaceBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(woodenFurnaceTag));
                output.accept(woodenFurnaceBlock);
                
                // 石制砧板熔炉
                ItemStack stoneFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                CompoundTag stoneFurnaceTag = new CompoundTag();
                stoneFurnaceTag.putInt("ManaitaType", 2);
                stoneFurnaceBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(stoneFurnaceTag));
                output.accept(stoneFurnaceBlock);
                
                // 铁制砧板熔炉
                ItemStack ironFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                CompoundTag ironFurnaceTag = new CompoundTag();
                ironFurnaceTag.putInt("ManaitaType", 3);
                ironFurnaceBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(ironFurnaceTag));
                output.accept(ironFurnaceBlock);
                
                // 金制砧板熔炉
                ItemStack goldFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                CompoundTag goldFurnaceTag = new CompoundTag();
                goldFurnaceTag.putInt("ManaitaType", 4);
                goldFurnaceBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(goldFurnaceTag));
                output.accept(goldFurnaceBlock);
                
                // 钻石制砧板熔炉
                ItemStack diamondFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                CompoundTag diamondFurnaceTag = new CompoundTag();
                diamondFurnaceTag.putInt("ManaitaType", 5);
                diamondFurnaceBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(diamondFurnaceTag));
                output.accept(diamondFurnaceBlock);
                
                // 绿宝石制砧板熔炉
                ItemStack emeraldFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                CompoundTag emeraldFurnaceTag = new CompoundTag();
                emeraldFurnaceTag.putInt("ManaitaType", 6);
                emeraldFurnaceBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(emeraldFurnaceTag));
                output.accept(emeraldFurnaceBlock);
                
                // 红石制砧板熔炉
                ItemStack redstoneFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                CompoundTag redstoneFurnaceTag = new CompoundTag();
                redstoneFurnaceTag.putInt("ManaitaType", 7);
                redstoneFurnaceBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(redstoneFurnaceTag));
                output.accept(redstoneFurnaceBlock);
                
                // 下界合金制砧板熔炉
                ItemStack netheriteFurnaceBlock = new ItemStack(ModItems.FURNACE_BLOCK.get());
                CompoundTag netheriteFurnaceTag = new CompoundTag();
                netheriteFurnaceTag.putInt("ManaitaType", 8);
                netheriteFurnaceBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(netheriteFurnaceTag));
                output.accept(netheriteFurnaceBlock);

                // 砧板钩[橡木]
                output.accept(ModItems.WOODEN_HOOK.get());
                
                // 木制砧板钩[白桦木]
                ItemStack woodenHookBlock = new ItemStack(ModItems.HOOK_BLOCK.get());
                output.accept(woodenHookBlock);
                
                // 石制砧板钩
                ItemStack stoneHookBlock = new ItemStack(ModItems.HOOK_BLOCK.get());
                CompoundTag stoneHookTag = new CompoundTag();
                stoneHookTag.putInt("ManaitaType", 1);
                stoneHookBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(stoneHookTag));
                output.accept(stoneHookBlock);
                
                // 铁制砧板钩
                ItemStack ironHookBlock = new ItemStack(ModItems.HOOK_BLOCK.get());
                CompoundTag ironHookTag = new CompoundTag();
                ironHookTag.putInt("ManaitaType", 2);
                ironHookBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(ironHookTag));
                output.accept(ironHookBlock);
                
                // 金制砧板钩
                ItemStack goldHookBlock = new ItemStack(ModItems.HOOK_BLOCK.get());
                CompoundTag goldHookTag = new CompoundTag();
                goldHookTag.putInt("ManaitaType", 3);
                goldHookBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(goldHookTag));
                output.accept(goldHookBlock);
                
                // 钻石制砧板钩
                ItemStack diamondHookBlock = new ItemStack(ModItems.HOOK_BLOCK.get());
                CompoundTag diamondHookTag = new CompoundTag();
                diamondHookTag.putInt("ManaitaType", 4);
                diamondHookBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(diamondHookTag));
                output.accept(diamondHookBlock);
                
                // 绿宝石制砧板钩
                ItemStack emeraldHookBlock = new ItemStack(ModItems.HOOK_BLOCK.get());
                CompoundTag emeraldHookTag = new CompoundTag();
                emeraldHookTag.putInt("ManaitaType", 5);
                emeraldHookBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(emeraldHookTag));
                output.accept(emeraldHookBlock);
                
                // 红石制砧板钩
                ItemStack redstoneHookBlock = new ItemStack(ModItems.HOOK_BLOCK.get());
                CompoundTag redstoneHookTag = new CompoundTag();
                redstoneHookTag.putInt("ManaitaType", 6);
                redstoneHookBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(redstoneHookTag));
                output.accept(redstoneHookBlock);
                
                // 下界合金制砧板钩
                ItemStack netheriteHookBlock = new ItemStack(ModItems.HOOK_BLOCK.get());
                CompoundTag netheriteHookTag = new CompoundTag();
                netheriteHookTag.putInt("ManaitaType", 7);
                netheriteHookBlock.set(DataComponents.CUSTOM_DATA, CustomData.of(netheriteHookTag));
                output.accept(netheriteHookBlock);

                // 砧板斧
                output.accept(ModItems.MANAITA_AXE.get());
                // 砧板稿
                output.accept(ModItems.MANAITA_PICKAXE.get());
                // 砧板铲
                output.accept(ModItems.MANAITA_SHOVEL.get());
                // 砧板锄
                output.accept(ModItems.MANAITA_HOE.get());
                // 砧板剪刀
                output.accept(ModItems.MANAITA_SHEARS.get());
                // 砧板弓
                output.accept(ModItems.MANAITA_BOW.get());
                // 砧板剑
                output.accept(ModItems.MANAITA_SWORD.get());
                // 砧板头盔
                output.accept(ModItems.MANAITA_HELMET.get());
                // 砧板胸甲
                output.accept(ModItems.MANAITA_CHESTPLATE.get());
                // 砧板护腿
                output.accept(ModItems.MANAITA_LEGGINGS.get());
                // 砧板靴子
                output.accept(ModItems.MANAITA_BOOTS.get());
                // 砧板多功能工具
                output.accept(ModItems.MANAITA_PAXEL.get());
                // 砧板之刃[神]
                output.accept(ModItems.MANAITA_SWORD_GOD.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}