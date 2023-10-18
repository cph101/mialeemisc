package xyz.amymialee.mialeemisc.cooldowns;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import xyz.amymialee.mialeemisc.MialeeMisc;

public class ServerIdentifierCooldownManager extends IdentifierCooldownManager {
    private final ServerPlayerEntity player;

    public ServerIdentifierCooldownManager(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override
    protected void onCooldownUpdate(Identifier identifier, int duration) {
        super.onCooldownUpdate(identifier, duration);
        var buf = PacketByteBufs.create();
        buf.writeIdentifier(identifier);
        buf.writeInt(duration);
        ServerPlayNetworking.send(this.player, MialeeMisc.cooldownPacket, buf);
    }

    @Override
    protected void onCooldownUpdate(Identifier identifier) {
        super.onCooldownUpdate(identifier);
        var buf = PacketByteBufs.create();
        buf.writeIdentifier(identifier);
        buf.writeInt(0);
        ServerPlayNetworking.send(this.player, MialeeMisc.cooldownPacket, buf);
    }
}