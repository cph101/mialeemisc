package xyz.amymialee.mialeemisc;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import xyz.amymialee.mialeemisc.cooldowns.IdentifierCooldownHolder;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class MialeeMiscClient implements ClientModInitializer {
    public static final Set<Item> INVENTORY_ITEMS = new ReferenceOpenHashSet<>();

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(MialeeMisc.floatyPacket, (minecraftClient, playNetworkHandler, packetByteBuf, packetSender) -> {
            var stack = packetByteBuf.readItemStack();
            minecraftClient.execute(() -> minecraftClient.gameRenderer.showFloatingItem(stack));
        });
        ClientPlayNetworking.registerGlobalReceiver(MialeeMisc.cooldownPacket, (minecraftClient, playNetworkHandler, packetByteBuf, packetSender) -> {
            var identifier = packetByteBuf.readIdentifier();
            var duration = packetByteBuf.readInt();
            minecraftClient.execute(() -> {
                if (minecraftClient.player instanceof IdentifierCooldownHolder holder) {
                    holder.getIdentifierCooldownManager().set(identifier, duration);
                }
            });
        });
    }
}