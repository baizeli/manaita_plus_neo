package net.manaita_plus_neo.common.network.server;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.common.util.ManaitaPlusUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageRemoveEntities implements CustomPacketPayload {
    public static final Type<MessageRemoveEntities> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "remove_entities"));
    public static final StreamCodec<FriendlyByteBuf, MessageRemoveEntities> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> msg.toBytes(buf),
            MessageRemoveEntities::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private final boolean unsafe;

    public MessageRemoveEntities(FriendlyByteBuf buffer) {
        this.unsafe = buffer.readBoolean();
    }

    public MessageRemoveEntities(boolean unsafe) {
        this.unsafe = unsafe;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(unsafe);
    }

    public void handler(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!ctx.flow().isClientbound()) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            ManaitaPlusUtils.godKill(mc.player, true, unsafe);
        });
    }
}