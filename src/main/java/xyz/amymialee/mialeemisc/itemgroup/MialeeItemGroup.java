package xyz.amymialee.mialeemisc.itemgroup;

import net.fabricmc.fabric.api.itemgroup.v1.IdentifiableItemGroup;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroup;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.amymialee.mialeemisc.MialeeMisc;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class MialeeItemGroup extends ItemGroup implements IdentifiableItemGroup, FabricItemGroup {
    private Function<Integer, ItemStack> stackSupplier = (i) -> this.displayStacks.toArray(new ItemStack[0])[i % this.displayStacks.size()];
    private Identifier identifier;
    private int page = -1;

    public MialeeItemGroup(Identifier identifier) {
        super(null, -1, ItemGroup.Type.CATEGORY, Text.empty(), () -> ItemStack.EMPTY, (enabledFeatures, entries, operatorEnabled) -> {});
        this.setId(identifier);
        ItemGroupHelper.appendItemGroup(this);
    }

    public static MialeeItemGroup create(Identifier identifier) {
        return new MialeeItemGroup(identifier);
    }

    public MialeeItemGroup addItems(ItemStack ... stacks) {
        this.displayStacks.addAll(Arrays.asList(stacks));
        return this;
    }

    public MialeeItemGroup addItems(Supplier<Collection<ItemStack>> supplier) {
        this.displayStacks.addAll(supplier.get());
        return this;
    }

    public MialeeItemGroup setItems(ItemStack ... stacks) {
        this.displayStacks.clear();
        this.displayStacks.addAll(Arrays.asList(stacks));
        return this;
    }

    public MialeeItemGroup setItems(Supplier<Collection<ItemStack>> supplier) {
        this.displayStacks.clear();
        this.displayStacks.addAll(supplier.get());
        return this;
    }

    public MialeeItemGroup setIcon(ItemStack ... stacks) {
        return this.setIcon((i) -> stacks[(i / 20) % stacks.length]);
    }

    public MialeeItemGroup setIcon(Function<Integer, ItemStack> stackFunction) {
        this.stackSupplier = stackFunction;
        return this;
    }

    public ItemStack createIcon(int index) {
        return this.stackSupplier.apply(index);
    }

    @Override
    public int getPage() {
        return this.page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Identifier getId() {
        if (this.identifier == null) {
            this.setId(MialeeMisc.id("unidentified_" + UUID.randomUUID()));
        }
        return this.identifier;
    }

    @Override
    public void setId(Identifier identifier) {
        this.identifier = identifier;
    }
}