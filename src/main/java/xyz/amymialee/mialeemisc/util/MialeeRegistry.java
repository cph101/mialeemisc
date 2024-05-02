package xyz.amymialee.mialeemisc.util;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class MialeeRegistry {
    public static Item register(Identifier id, Item item) {
        return Registry.register(Registries.ITEM, id, item);
    }

    public static SoundEvent register(SoundEvent sound) {
        return Registry.register(Registries.SOUND_EVENT, sound.getId(), sound);
    }

    public static StatusEffect register(Identifier id, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, id, effect);
    }

    public static Block register(Identifier id, Block block, ItemGroup ... groups) {
        for (ItemGroup group : groups) {
            ItemGroupEvents.modifyEntriesEvent(Registries.ITEM_GROUP.getKey(group).get()).register(content -> {
                content.add(block);
            });
        }
        return registerBlockItem(id, new BlockItem(block, new Item.Settings()));
    }

    public static Block registerBlockItem(Identifier id, BlockItem item) {
        register(id, item);
        return register(id, item.getBlock());
    }

    public static Block register(Identifier id, Block block) {
        Registry.register(Registries.BLOCK, id, block);
        return block;
    }

    public static Enchantment register(Identifier id, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, id, enchantment);
    }

    public static <T extends Entity> EntityType<T> register(Identifier id, EntityType<T> entity) {
        return Registry.register(Registries.ENTITY_TYPE, id, entity);
    }

    public static <T extends BlockEntity> BlockEntityType<T> register(Identifier id, BlockEntityType<T> blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(Identifier id, ScreenHandlerType<T> screenHandlerType) {
        return Registry.register(Registries.SCREEN_HANDLER, id, screenHandlerType);
    }

    public static ItemStack enchantStack(ItemStack stack, Enchantment enchantment, int level) {
        stack.addEnchantment(enchantment, level);
        return stack;
    }
}