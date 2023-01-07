package xyz.amymialee.mialeemisc.util;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
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
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class MialeeRegistry {
    public static Item register(Identifier id, Item item) {
        return Registry.register(Registry.ITEM, id, item);
    }

    public static SoundEvent register(SoundEvent sound) {
        return Registry.register(Registry.SOUND_EVENT, sound.getId(), sound);
    }

    public static StatusEffect register(Identifier id, StatusEffect effect) {
        return Registry.register(Registry.STATUS_EFFECT, id, effect);
    }

    private static Block register(Identifier id, Block block, ItemGroup group) {
        return registerBlockItem(id, new BlockItem(block, new FabricItemSettings().group(group)));
    }

    private static Block registerBlockItem(Identifier id, BlockItem item) {
        register(id, item);
        return register(id, item.getBlock());
    }

    private static Block register(Identifier id, Block block) {
        Registry.register(Registry.BLOCK, id, block);
        return block;
    }

    private static Enchantment register(Identifier id, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, id, enchantment);
    }

    public static <T extends Entity> EntityType<T> register(Identifier id, EntityType<T> entity) {
        return Registry.register(Registry.ENTITY_TYPE, id, entity);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(Identifier id, BlockEntityType<T> blockEntityType) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(Identifier id, ScreenHandlerType<T> screenHandlerType) {
        return Registry.register(Registry.SCREEN_HANDLER, id, screenHandlerType);
    }

    public static ItemStack enchantStack(ItemStack stack, Enchantment enchantment, int level) {
        stack.addEnchantment(enchantment, level);
        return stack;
    }
}