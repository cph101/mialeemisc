package xyz.amymialee.mialeemisc.cooldowns;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import xyz.amymialee.mialeemisc.MialeeMisc;
import xyz.amymialee.mialeemisc.network.CooldownPacket;

public class ServerIdentifierCooldownManager extends IdentifierCooldownManager {
    private final ServerPlayerEntity player;

    public ServerIdentifierCooldownManager(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override
    protected void onCooldownUpdate(Identifier identifier, int duration) {
        super.onCooldownUpdate(identifier, duration);
        ServerPlayNetworking.send(this.player, new CooldownPacket(identifier, duration));
    }

    @Override
    protected void onCooldownUpdate(Identifier identifier) {
        super.onCooldownUpdate(identifier);
        ServerPlayNetworking.send(this.player, new CooldownPacket(identifier, 0));
    }
}