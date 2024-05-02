package xyz.amymialee.mialeemisc.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import xyz.amymialee.mialeemisc.MialeeMisc;

public record CooldownPacket(Identifier id, int duration) implements CustomPayload {
    public static final CustomPayload.Id<CooldownPacket> ID = MialeeMisc.networkId("cooldown");
    public static final PacketCodec<PacketByteBuf, CooldownPacket> CODEC = CustomPayload.codecOf(CooldownPacket::write, CooldownPacket::new);

    private CooldownPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readInt());
    }

    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(id);
        buf.writeInt(duration);
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
