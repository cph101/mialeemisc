package xyz.amymialee.mialeemisc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class MialeeMiscClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(MialeeMisc.floatyPacket, (minecraftClient, playNetworkHandler, packetByteBuf, packetSender) -> {
            ItemStack stack = packetByteBuf.readItemStack();
            minecraftClient.execute(() -> minecraftClient.gameRenderer.showFloatingItem(stack));
        });
    }
}
