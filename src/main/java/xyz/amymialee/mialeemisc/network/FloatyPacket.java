package xyz.amymialee.mialeemisc.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import xyz.amymialee.mialeemisc.MialeeMisc;

public record FloatyPacket(ItemStack stack) implements CustomPayload {
    public static final CustomPayload.Id<FloatyPacket> ID = MialeeMisc.networkId("floaty");
    public static final PacketCodec<PacketByteBuf, FloatyPacket> CODEC = CustomPayload.codecOf(FloatyPacket::write, FloatyPacket::new);

    private FloatyPacket(PacketByteBuf buf) {
        this(new ItemStack(Registries.ITEM.get(buf.readIdentifier()), buf.readInt()));
    }

    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(Registries.ITEM.getId(stack.getItem()));
        buf.writeInt(stack.getCount());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
