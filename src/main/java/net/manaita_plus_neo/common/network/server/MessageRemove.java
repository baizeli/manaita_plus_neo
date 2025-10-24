package net.manaita_plus_neo.common.network.server;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.common.util.ManaitaPlusUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageRemove implements CustomPacketPayload {
    public static final Type<MessageRemove> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "remove"));
    public static final StreamCodec<FriendlyByteBuf, MessageRemove> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> msg.toBytes(buf),
            MessageRemove::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private final int id;

    public MessageRemove(FriendlyByteBuf buffer) {
        this.id = buffer.readInt();
    }

    public MessageRemove(int id) {
        this.id = id;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
    }

    public void handler(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!ctx.flow().isClientbound()) return;
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(id);
            if (entity == null) return;
            ManaitaPlusUtils.removeOnClient(entity);
        });
    }
}