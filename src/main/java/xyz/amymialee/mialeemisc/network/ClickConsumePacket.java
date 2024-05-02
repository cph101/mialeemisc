package xyz.amymialee.mialeemisc.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import xyz.amymialee.mialeemisc.MialeeMisc;

public record ClickConsumePacket() implements CustomPayload {
    public static final CustomPayload.Id<ClickConsumePacket> ID = MialeeMisc.networkId("click_consume");
    public static final PacketCodec<PacketByteBuf, ClickConsumePacket> CODEC = CustomPayload.codecOf(ClickConsumePacket::write, ClickConsumePacket::new);

    private ClickConsumePacket(PacketByteBuf buf) {
        this();
    }

    public void write(PacketByteBuf buf) {}
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

