package net.manaita_plus_neo;

import net.manaita_plus_neo.block.ModBlocks;
import net.manaita_plus_neo.block.entity.ModBlockEntities;
import net.manaita_plus_neo.client.ClientSetupEvents;
import net.manaita_plus_neo.common.menu.ModMenuTypes;
import net.manaita_plus_neo.core.util.Helper;
import net.manaita_plus_neo.entity.ModEntities;
import net.manaita_plus_neo.item.ModItems;
import net.manaita_plus_neo.network.Networking;
import net.manaita_plus_neo.util.EntityUtils;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(ManaitaPlusNeo.MOD_ID)
public class ManaitaPlusNeo
{
    public static final String MOD_ID = "manaita_plus_neo";

    public ManaitaPlusNeo(IEventBus modEventBus, ModContainer modContainer) {
        try {
            Class.forName(EntityUtils.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        //modEventBus.addListener(Networking::register);
        modEventBus.addListener(net.manaita_plus_neo.common.network.Networking::register);
        modEventBus.addListener(ClientSetupEvents::clientSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}