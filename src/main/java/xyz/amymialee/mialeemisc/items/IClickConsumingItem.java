package xyz.amymialee.mialeemisc.items;

import net.minecraft.server.network.ServerPlayerEntity;

public interface IClickConsumingItem {
    void mialeeMisc$doAttack(ServerPlayerEntity player);
}