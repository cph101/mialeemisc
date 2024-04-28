package xyz.amymialee.mialeemisc.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.MialeeMisc;

import java.util.Optional;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "getFirstMatch(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/Optional;", at = @At(value = "RETURN"), cancellable = true)
    private <C extends Inventory, T extends Recipe<C>> void mialeeMisc$killRecipes(RecipeType<T> type, C inventory, World world, CallbackInfoReturnable<Optional<T>> cir) {
        var optional = cir.getReturnValue();
        if (optional.isPresent() && optional.get().getOutput(world.getRegistryManager()).isIn(MialeeMisc.UNCRAFTABLE)) {
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(method = "getFirstMatch(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;Lnet/minecraft/util/Identifier;)Ljava/util/Optional;", at = @At(value = "RETURN"), cancellable = true)
    private <C extends Inventory, T extends Recipe<C>> void mialeeMisc$killRecipes(RecipeType<T> type, C inventory, World world, @Nullable Identifier id, CallbackInfoReturnable<Optional<Pair<Identifier, T>>> cir) {
        var optional = cir.getReturnValue();
        if (optional.isPresent() && optional.get().getSecond().getOutput(world.getRegistryManager()).isIn(MialeeMisc.UNCRAFTABLE)) {
            cir.setReturnValue(Optional.empty());
        }
    }
}