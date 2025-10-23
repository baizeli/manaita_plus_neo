package net.manaita_plus_neo.common.menu;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ManaitaPlusNeo.MOD_ID);

    // 砧板工作台
    public static final DeferredHolder<MenuType<?>, MenuType<ManaitaCraftingMenu>> CRAFTING_MANAITA = 
        MENUS.register("crafting_manaita", () -> new MenuType<>((containerId, playerInventory) -> 
            new ManaitaCraftingMenu(containerId, playerInventory, ContainerLevelAccess.NULL),
            FeatureFlags.DEFAULT_FLAGS
        )
    );

    // 砧板熔炉
    public static final DeferredHolder<MenuType<?>, MenuType<ManaitaFurnaceMenu>> FURNACE_MANAITA = 
        MENUS.register("furnace_manaita", () -> new MenuType<>((containerId, playerInventory) -> 
            new ManaitaFurnaceMenu(containerId, playerInventory), 
            FeatureFlags.DEFAULT_FLAGS
        )
    );
}