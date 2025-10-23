package net.manaita_plus_neo.network;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.network.client.MessageKey;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ManaitaPlusNeo.MOD_ID);
        registrar.playToServer(MessageKey.TYPE, MessageKey.STREAM_CODEC, MessageKey::handle);
    }
}