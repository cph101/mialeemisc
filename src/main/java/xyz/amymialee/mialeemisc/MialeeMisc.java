package xyz.amymialee.mialeemisc;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.mialeemisc.client.InventoryItemRenderer;
import xyz.amymialee.mialeemisc.itemgroup.MialeeItemGroup;
import xyz.amymialee.mialeemisc.items.IAutoSmeltingItem;
import xyz.amymialee.mialeemisc.items.IClickConsumingItem;
import xyz.amymialee.mialeemisc.util.AutoSmeltingCallback;
import xyz.amymialee.mialeemisc.util.MialeeMath;
import xyz.amymialee.mialeemisc.util.PlayerTargeting;

import java.util.Set;

@SuppressWarnings("unused")
public class MialeeMisc implements ModInitializer {
    public static final String MOD_ID = "mialeemisc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Set<Item> INVENTORY_ITEMS = new ReferenceOpenHashSet<>();
    public static final Identifier clickConsumePacket = id("click_consume");
    public static final Identifier targetPacket = id("target");
    public static final Identifier floatyPacket = id("floaty");
    public static final TagKey<Item> DAMAGE_IMMUNE = TagKey.of(Registry.ITEM_KEY, id("damage_immune"));
    public static final TagKey<Item> NETHERITE_TOOLS = TagKey.of(Registry.ITEM_KEY, id("netherite_tools"));
    public static final TagKey<Item> UNCRAFTABLE = TagKey.of(Registry.ITEM_KEY, id("uncraftable"));
    public static final GameRules.Key<GameRules.BooleanRule> FIRE_ASPECT_AUTOSMELTING = GameRuleRegistry.register("mialeemisc:fireaspectsmelting", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> DISABLE_RECIPES_GAMERULE = GameRuleRegistry.register("mialeemisc:disable_recipes_tag", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(targetPacket, (minecraftServer, serverPlayer, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int id = packetByteBuf.readInt();
            minecraftServer.execute(() -> {
                if (serverPlayer instanceof PlayerTargeting targeting) {
                    if (serverPlayer.getWorld().getEntityById(id) instanceof LivingEntity living) {
                        targeting.mialeeMisc$setLastTarget(living);
                    }
                }
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(clickConsumePacket, (minecraftServer, serverPlayer, serverPlayNetworkHandler, packetByteBuf, packetSender) -> minecraftServer.execute(() -> {
            if (serverPlayer.getMainHandStack().getItem() instanceof IClickConsumingItem item) {
                item.mialeeMisc$doAttack(serverPlayer);
            }
        }));
        AutoSmeltingCallback.EVENT.register((stateX, worldX, posX, blockEntityX, entityX, stackX) -> {
            if (stackX.getItem() instanceof IAutoSmeltingItem autoSmeltingItem && autoSmeltingItem.mialeeMisc$shouldSmelt(stackX, stateX)) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
        AutoSmeltingCallback.EVENT.register((stateX, worldX, posX, blockEntityX, entityX, stackX) -> {
            if (EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stackX) > 0 && worldX.getGameRules().getBoolean(MialeeMisc.FIRE_ASPECT_AUTOSMELTING)) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.info("Loaded in development environment.");
            MialeeItemGroup.create(id("mialee_group"))
                    .setIcon((i) -> {
                        Item item = Registry.ITEM.get(MialeeMath.clampLoop(i / 2 + 1, 1, Registry.ITEM.size()));
                        return new ItemStack(item);
                    })
                    .setItems((itemStacks, itemGroup) -> {
                        for(Item item : Registry.ITEM) {
                            if (item == Items.AIR) continue;
                            itemStacks.add(item.getDefaultStack());
                        }
                    });
        }
    }

    public static void registerInventoryItem(Item item) {
        Identifier itemId = Registry.ITEM.getId(item);
        InventoryItemRenderer inventoryItemRenderer = new InventoryItemRenderer(itemId);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(inventoryItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(item, inventoryItemRenderer);
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(itemId, "inventory"));
            out.accept(new ModelIdentifier(itemId + "_handheld", "inventory"));
        });
        INVENTORY_ITEMS.add(item);
    }

    public static ItemStack enchantStack(ItemStack stack, Enchantment enchantment, int level) {
        stack.addEnchantment(enchantment, level);
        return stack;
    }

    public static <T> Registry<T> createRegistry(Identifier id, Class<T> clazz) {
        return FabricRegistryBuilder.createSimple(clazz, id).buildAndRegister();
    }

    public static Identifier id(String ... path) {
        return namedId(MOD_ID, path);
    }

    public static Identifier namedId(String namespace, String ... path) {
        return new Identifier(namespace, String.join(".", path));
    }
}