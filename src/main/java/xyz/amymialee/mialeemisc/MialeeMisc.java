package xyz.amymialee.mialeemisc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.mialeemisc.entities.IPlayerTargeting;
import xyz.amymialee.mialeemisc.events.AutoSmeltingCallback;
import xyz.amymialee.mialeemisc.itemgroup.MialeeItemGroup;
import xyz.amymialee.mialeemisc.items.IAutoSmeltingItem;
import xyz.amymialee.mialeemisc.items.IClickConsumingItem;
import xyz.amymialee.mialeemisc.util.MialeeMath;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class MialeeMisc implements ModInitializer {
    public static final String MOD_ID = "mialeemisc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier clickConsumePacket = id("click_consume");
    public static final Identifier targetPacket = id("target");
    public static final Identifier floatyPacket = id("floaty");
    public static final Identifier cooldownPacket = id("cooldown");
    public static final TagKey<Item> DAMAGE_IMMUNE = TagKey.of(Registries.ITEM.getKey(), id("damage_immune"));
    public static final TagKey<Item> NETHERITE_TOOLS = TagKey.of(Registries.ITEM.getKey(), id("netherite_tools"));
    public static final TagKey<Item> UNCRAFTABLE = TagKey.of(Registries.ITEM.getKey(), id("uncraftable"));
    public static final TagKey<Item> UNBREAKABLE = TagKey.of(Registries.ITEM.getKey(), id("unbreakable"));
    public static final GameRules.Key<GameRules.BooleanRule> FIRE_ASPECT_AUTOSMELTING = GameRuleRegistry.register("mialeemisc:fireaspectsmelting", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(targetPacket, (minecraftServer, serverPlayer, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int id = packetByteBuf.readInt();
            minecraftServer.execute(() -> {
                if (serverPlayer instanceof IPlayerTargeting targeting) {
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
        }
        MialeeItemGroup.create(id("mialee_group"))
                .setIcon((i) -> {
                    Item item = Registries.ITEM.get(MialeeMath.clampLoop(i / 2 + 1, 1, Registries.ITEM.size()));
                    return new ItemStack(item);
                })
                .setItems(() -> {
                    ArrayList<ItemStack> itemStacks = new ArrayList<>();
                    for(Item item : Registries.ITEM) {
                        if (item == Items.AIR) continue;
                        itemStacks.add(item.getDefaultStack());
                    }
                    return itemStacks;
                });
    }

    public static ItemStack enchantStack(ItemStack stack, EnchantmentLevelEntry ... entry) {
        for (EnchantmentLevelEntry enchantmentLevelEntry : entry) {
            stack.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
        }
        return stack;
    }

    public static ItemStack enchantedBook(EnchantmentLevelEntry ... entry) {
        ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
        for (EnchantmentLevelEntry enchantmentLevelEntry : entry) {
            EnchantedBookItem.addEnchantment(stack, enchantmentLevelEntry);
        }
        return stack;
    }

    public static Identifier id(String ... path) {
        return namedId(MOD_ID, path);
    }

    public static Identifier namedId(String namespace, String ... path) {
        return new Identifier(namespace, String.join(".", path));
    }
}