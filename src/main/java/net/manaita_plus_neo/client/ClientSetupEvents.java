package net.manaita_plus_neo.client;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.client.gui.CraftingManaitaScreen;
import net.manaita_plus_neo.client.gui.FurnaceManaitaScreen;
import net.manaita_plus_neo.common.menu.ModMenuTypes;
import net.manaita_plus_neo.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = ManaitaPlusNeo.MOD_ID)
public class ClientSetupEvents {
    
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        // 砧板工作台
        event.register(ModMenuTypes.CRAFTING_MANAITA.get(), CraftingManaitaScreen::new);
        // 砧板熔炉
        event.register(ModMenuTypes.FURNACE_MANAITA.get(), FurnaceManaitaScreen::new);
    }
    
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 砧板工作台类型
            ItemProperties.register(ModItems.CRAFTING_BLOCK.get(),
                    ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "manaita_type"),
                    (stack, level, entity, seed) -> {
                        var customData = stack.get(DataComponents.CUSTOM_DATA);
                        if (customData != null) {
                            var tag = customData.getUnsafe();
                            if (tag.contains("ManaitaType")) {
                                return tag.getInt("ManaitaType");
                            }
                        }
                        return 0;
                    }
            );
            
            // 砧板熔炉类型
            ItemProperties.register(ModItems.FURNACE_BLOCK.get(),
                    ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "manaita_type"),
                    (stack, level, entity, seed) -> {
                        var customData = stack.get(DataComponents.CUSTOM_DATA);
                        if (customData != null) {
                            var tag = customData.getUnsafe();
                            if (tag.contains("ManaitaType")) {
                                return tag.getInt("ManaitaType");
                            }
                        }
                        return 0;
                    }
            );
            
            // 砧板钩类型
            ItemProperties.register(ModItems.HOOK_BLOCK.get(),
                    ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "manaita_type"),
                    (stack, level, entity, seed) -> {
                        var customData = stack.get(DataComponents.CUSTOM_DATA);
                        if (customData != null) {
                            var tag = customData.getUnsafe();
                            if (tag.contains("ManaitaType")) {
                                return tag.getInt("ManaitaType");
                            }
                        }
                        return 0;
                    }
            );
        });
    }
}