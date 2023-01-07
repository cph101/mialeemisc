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
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.amymialee.mialeemisc.client.InventoryItemRenderer;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class MialeeMiscClient implements ClientModInitializer {
    public static final Set<Item> INVENTORY_ITEMS = new ReferenceOpenHashSet<>();

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(MialeeMisc.floatyPacket, (minecraftClient, playNetworkHandler, packetByteBuf, packetSender) -> {
            ItemStack stack = packetByteBuf.readItemStack();
            minecraftClient.execute(() -> minecraftClient.gameRenderer.showFloatingItem(stack));
        });
    }

    public void registerInventoryItem(Item item) {
        INVENTORY_ITEMS.add(item);
        Identifier itemId = Registry.ITEM.getId(item);
        InventoryItemRenderer inventoryItemRenderer = new InventoryItemRenderer(itemId);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(inventoryItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(item, inventoryItemRenderer);
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(itemId, "inventory"));
            out.accept(new ModelIdentifier(itemId + "_handheld", "inventory"));
        });
    }
}
