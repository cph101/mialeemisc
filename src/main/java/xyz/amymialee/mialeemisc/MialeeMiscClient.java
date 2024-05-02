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
import xyz.amymialee.mialeemisc.client.InventoryItemRenderer;
import xyz.amymialee.mialeemisc.cooldowns.IdentifierCooldownHolder;
import xyz.amymialee.mialeemisc.network.CooldownPacket;
import xyz.amymialee.mialeemisc.network.FloatyPacket;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class MialeeMiscClient implements ClientModInitializer {
    public static final Set<Item> INVENTORY_ITEMS = new ReferenceOpenHashSet<>();

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(FloatyPacket.ID, (payload, context) -> {
            ItemStack stack = payload.stack();
            context.client().execute(() -> context.client().gameRenderer.showFloatingItem(stack));
        });
        ClientPlayNetworking.registerGlobalReceiver(CooldownPacket.ID, (payload, context) -> {
            Identifier identifier = payload.id();
            int duration = payload.duration();
            context.client().execute(() -> {
                if (context.player() instanceof IdentifierCooldownHolder holder) {
                    holder.getIdentifierCooldownManager().set(identifier, duration);
                }
            });
        });
    }

    public static void registerInventoryItem(Item item) {
        Identifier itemId = Registries.ITEM.getId(item);
        InventoryItemRenderer inventoryItemRenderer = new InventoryItemRenderer(itemId);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(inventoryItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(item, inventoryItemRenderer);
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(itemId, "inventory"));
            out.accept(new ModelIdentifier(new Identifier(itemId.getNamespace(), itemId.getPath() + "_handheld"), "inventory"));
        });
        INVENTORY_ITEMS.add(item);
    }
}