package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.ICustomEnchantTargetsItem;

import java.util.Arrays;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    public void mialeeMisc$acceptable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof ICustomEnchantTargetsItem enchants) {
            if (Arrays.stream(enchants.mialeeMisc$getSupportedEnchants()).toList().contains(this)) {
                cir.setReturnValue(true);
            }
            cir.setReturnValue(false);
        }
    }
}