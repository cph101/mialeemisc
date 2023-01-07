package xyz.amymialee.mialeemisc.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public interface IAutoSmeltingItem {
    boolean mialeeMisc$shouldSmelt(ItemStack stack, BlockState state);
}