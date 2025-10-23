package net.manaita_plus_neo;

import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = ManaitaPlusNeo.MOD_ID)
public class ManaitaKeyBindings {
    
    public static KeyMapping manaitaKey;
    public static KeyMapping manaitaHelmetKey;
    public static KeyMapping manaitaChestplateKey;
    public static KeyMapping manaitaLeggingsKey;
    public static KeyMapping manaitaBootsKey;

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        manaitaKey = new KeyMapping(
            "key.manaita", 
            org.lwjgl.glfw.GLFW.GLFW_KEY_X, 
            "key.categories.manaita_plus_neo"
        );
        event.register(manaitaKey);
        
        manaitaHelmetKey = new KeyMapping(
            "key.manaita.helmet", 
            org.lwjgl.glfw.GLFW.GLFW_KEY_H, 
            "key.categories.manaita_plus_neo"
        );
        event.register(manaitaHelmetKey);
        
        manaitaChestplateKey = new KeyMapping(
            "key.manaita.chestplate", 
            org.lwjgl.glfw.GLFW.GLFW_KEY_J, 
            "key.categories.manaita_plus_neo"
        );
        event.register(manaitaChestplateKey);
        
        manaitaLeggingsKey = new KeyMapping(
            "key.manaita.leggings", 
            org.lwjgl.glfw.GLFW.GLFW_KEY_K, 
            "key.categories.manaita_plus_neo"
        );
        event.register(manaitaLeggingsKey);
        
        manaitaBootsKey = new KeyMapping(
            "key.manaita.boots", 
            org.lwjgl.glfw.GLFW.GLFW_KEY_N, 
            "key.categories.manaita_plus_neo"
        );
        event.register(manaitaBootsKey);
    }
}