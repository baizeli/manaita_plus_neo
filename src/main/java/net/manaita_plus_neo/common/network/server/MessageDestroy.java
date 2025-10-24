package net.manaita_plus_neo.common.network.server;

import net.manaita_plus_neo.ManaitaPlusNeo;
import net.manaita_plus_neo.common.util.ManaitaPlusUtils;
import net.manaita_plus_neo.item.data.IManaitaPlusDestroy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageDestroy implements CustomPacketPayload {
    public static final Type<MessageDestroy> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "destroy"));
    public static final StreamCodec<FriendlyByteBuf, MessageDestroy> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> msg.toBytes(buf),
            MessageDestroy::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private final BlockPos blockPos;
    private final int range;
    private final Item item;

    public MessageDestroy(FriendlyByteBuf buffer) {
        blockPos = buffer.readBlockPos();
        range = buffer.readInt();
        item = BuiltInRegistries.ITEM.byId(buffer.readVarInt());
    }

    public MessageDestroy(BlockPos blockPos, int range, Item item) {
        this.blockPos = blockPos;
        this.range = range;
        this.item = item;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(range);
        buf.writeVarInt(BuiltInRegistries.ITEM.getId(item));
    }

    public void handler(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!ctx.flow().isClientbound()) return;
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level == null || mc.player == null) return;
            if (item instanceof IManaitaPlusDestroy des) {
                int xM = blockPos.getX() + range;
                int yM = blockPos.getY() + range;
                int zM = blockPos.getZ() + range;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for (int x = blockPos.getX() - range; x <= xM; x++) {
                    for (int y = blockPos.getY() - range; y <= yM; y++) {
                        for (int z = blockPos.getZ() - range; z <= zM; z++) {
                            BlockState blockState = level.getBlockState(mutableBlockPos.set(x, y, z));
                            if (blockState == null || !des.accept(blockState)) continue;

                            ManaitaPlusUtils.setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock(), 10);

                            SoundType soundtype = blockState.getSoundType(level, mutableBlockPos, mc.player);
                            mc.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), mutableBlockPos));
                        }
                    }
                }
            }
        });
    }
}