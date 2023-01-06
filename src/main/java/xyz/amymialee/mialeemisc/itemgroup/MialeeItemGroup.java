package xyz.amymialee.mialeemisc.itemgroup;

import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MialeeItemGroup extends ItemGroup {
    private Function<Integer, ItemStack> stackSupplier = (i) -> ItemStack.EMPTY;
    private BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay;
    private int swapInterval = 20;

    public MialeeItemGroup(Identifier identifier) {
        super(ItemGroup.GROUPS.length - 1, String.format("%s.%s", identifier.getNamespace(), identifier.getPath()));
    }

    public static MialeeItemGroup create(Identifier identifier) {
        Objects.requireNonNull(identifier, "identifier cannot be null");
        ((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
        return new MialeeItemGroup(identifier);
    }

    public MialeeItemGroup setIcon(ItemStack ... stacks) {
        return this.setIcon((i) -> stacks[(i / this.swapInterval) % stacks.length]);
    }

    public MialeeItemGroup setIcon(Function<Integer, ItemStack> stackFunction) {
        Objects.requireNonNull(stackFunction, "icon cannot be null");
        this.stackSupplier = stackFunction;
        return this;
    }

    public MialeeItemGroup setSwapInterval(int swapInterval) {
        this.swapInterval = Math.max(1, swapInterval);
        return this;
    }

    public MialeeItemGroup setItems(BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay) {
        this.stacksForDisplay = stacksForDisplay;
        return this;
    }

    @Override
    public void appendStacks(DefaultedList<ItemStack> stacks) {
        if (this.stacksForDisplay != null) {
            this.stacksForDisplay.accept(stacks, this);
            return;
        }
        super.appendStacks(stacks);
    }

    public ItemStack createIcon(int index) {
        return this.stackSupplier.apply(index);
    }

    @Override
    public ItemStack createIcon() {
        return this.stackSupplier.apply(0);
    }
}