package xyz.amymialee.mialeemisc.items;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public interface IClickConsumingItem {
    void mialeeMisc$doAttack(ServerPlayerEntity player);
}