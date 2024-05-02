package xyz.amymialee.mialeemisc.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import xyz.amymialee.mialeemisc.MialeeMisc;

public record TargetPacket(int entityId) implements CustomPayload {
    public static final CustomPayload.Id<TargetPacket> ID = MialeeMisc.<TargetPacket>networkId("target");
    public static final PacketCodec<PacketByteBuf, TargetPacket> CODEC = CustomPayload.codecOf(TargetPacket::write, TargetPacket::new);

    private TargetPacket(PacketByteBuf buf) {
        this(
                buf.readInt()
        );
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
