package net.manaita_plus_neo.common.network;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.common.network.client.MessageKey;
import net.manaita_plus_neo.common.network.server.MessageDestroy;
import net.manaita_plus_neo.common.network.server.MessageRemove;
import net.manaita_plus_neo.common.network.server.MessageRemoveEntities;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {

    public static final ResourceLocation KEY_CHANNEL = ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "key");
    public static final ResourceLocation DESTROY_CHANNEL = ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "destroy");
    public static final ResourceLocation REMOVE_CHANNEL = ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "remove");
    public static final ResourceLocation REMOVE_ENTITIES_CHANNEL = ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "remove_entities");

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                MessageKey.TYPE,
                MessageKey.STREAM_CODEC,
                (payload, context) -> payload.handler(context)
        );

        registrar.playToClient(
                MessageDestroy.TYPE,
                MessageDestroy.STREAM_CODEC,
                (payload, context) -> payload.handler(context)
        );

        registrar.playToClient(
                MessageRemove.TYPE,
                MessageRemove.STREAM_CODEC,
                (payload, context) -> payload.handler(context)
        );

        registrar.playToClient(
                MessageRemoveEntities.TYPE,
                MessageRemoveEntities.STREAM_CODEC,
                (payload, context) -> payload.handler(context)
        );
    }
}