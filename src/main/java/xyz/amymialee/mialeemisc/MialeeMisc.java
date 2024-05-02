package xyz.amymialee.mialeemisc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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
import xyz.amymialee.mialeemisc.network.ClickConsumePacket;
import xyz.amymialee.mialeemisc.network.CooldownPacket;
import xyz.amymialee.mialeemisc.network.FloatyPacket;
import xyz.amymialee.mialeemisc.network.TargetPacket;
import xyz.amymialee.mialeemisc.util.MialeeMath;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class MialeeMisc implements ModInitializer {
    public static final String MOD_ID = "mialeemisc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final TagKey<Item> DAMAGE_IMMUNE = TagKey.of(Registries.ITEM.getKey(), id("damage_immune"));
    public static final TagKey<Item> NETHERITE_TOOLS = TagKey.of(Registries.ITEM.getKey(), id("netherite_tools"));
    public static final TagKey<Item> UNCRAFTABLE = TagKey.of(Registries.ITEM.getKey(), id("uncraftable"));
    public static final TagKey<Item> UNBREAKABLE = TagKey.of(Registries.ITEM.getKey(), id("unbreakable"));
    public static final GameRules.Key<GameRules.BooleanRule> FIRE_ASPECT_AUTOSMELTING = GameRuleRegistry.register("mialeemisc:fireaspectsmelting", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(TargetPacket.ID, TargetPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(ClickConsumePacket.ID, ClickConsumePacket.CODEC);

        PayloadTypeRegistry.playS2C().register(FloatyPacket.ID, FloatyPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CooldownPacket.ID, CooldownPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TargetPacket.ID, (payload, context) -> {
            int id = payload.entityId();
            context.player().getServer().execute(() -> {
                if (context.player() instanceof IPlayerTargeting targeting) {
                    if (context.player().getWorld().getEntityById(id) instanceof LivingEntity living) {
                        targeting.mialeeMisc$setLastTarget(living);
                    }
                }
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(ClickConsumePacket.ID, (payload, context) -> context.player().getServer().execute(() -> {
            if (context.player().getMainHandStack().getItem() instanceof IClickConsumingItem item) {
                item.mialeeMisc$doAttack(context.player());
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
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        Arrays.stream(entry).forEach((levelEntry) -> builder.add(levelEntry.enchantment, levelEntry.level));
        stack.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
        return stack;
    }

    public static Identifier id(String ... path) {
        return namedId(MOD_ID, path);
    }

    public static <T extends CustomPayload> CustomPayload.Id<T> networkId(String ... path) {
        return new CustomPayload.Id<>(namedId(MOD_ID, path));
    }

    public static Identifier namedId(String namespace, String ... path) {
        return new Identifier(namespace, String.join(".", path));
    }
}