package net.manaita_plus_neo.client.event;

import net.manaita_plus_neo.ManaitaKeyBindings;
import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.item.data.IManaitaPlusKey;
import net.manaita_plus_neo.network.client.MessageKey;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = ManaitaPlusNeo.MOD_ID)
public class ManaitaClientEvents {
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (mc.player == null) return;
        
        // 翻倍
        if (ManaitaKeyBindings.manaitaKey.consumeClick()) {
            ItemStack mainHandItem = mc.player.getMainHandItem();
            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusKey iManaitaPlusKey) {
                iManaitaPlusKey.onManaitaKeyPressOnClient(mainHandItem, mc.player);
                PacketDistributor.sendToServer(new MessageKey((byte)0));
            }
        }
        
        // 头盔
        if (ManaitaKeyBindings.manaitaHelmetKey.consumeClick()) {
            ItemStack helmet = mc.player.getInventory().armor.get(3);
            if (!helmet.isEmpty() && helmet.getItem() instanceof IManaitaPlusKey helmetKey) {
                helmetKey.onManaitaKeyPressOnClient(helmet, mc.player);
                PacketDistributor.sendToServer(new MessageKey((byte)2));
            }
        }
        
        // 护甲
        if (ManaitaKeyBindings.manaitaChestplateKey.consumeClick()) {
            ItemStack chestplate = mc.player.getInventory().armor.get(2);
            if (!chestplate.isEmpty() && chestplate.getItem() instanceof IManaitaPlusKey chestplateKey) {
                chestplateKey.onManaitaKeyPressOnClient(chestplate, mc.player);
                PacketDistributor.sendToServer(new MessageKey((byte)3));
            }
        }
        
        // 护腿
        if (ManaitaKeyBindings.manaitaLeggingsKey.consumeClick()) {
            ItemStack leggings = mc.player.getInventory().armor.get(1);
            if (!leggings.isEmpty() && leggings.getItem() instanceof IManaitaPlusKey leggingsKey) {
                leggingsKey.onManaitaKeyPressOnClient(leggings, mc.player);
                PacketDistributor.sendToServer(new MessageKey((byte)4));
            }
        }
        
        // 靴子
        if (ManaitaKeyBindings.manaitaBootsKey.consumeClick()) {
            ItemStack boots = mc.player.getInventory().armor.get(0);
            if (!boots.isEmpty() && boots.getItem() instanceof IManaitaPlusKey bootsKey) {
                bootsKey.onManaitaKeyPressOnClient(boots, mc.player);
                PacketDistributor.sendToServer(new MessageKey((byte)5));
            }
        }
    }
}