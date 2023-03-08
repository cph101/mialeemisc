package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.MialeeMisc;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();
    @Shadow public abstract boolean isIn(TagKey<Item> tag);

    @Inject(method = "isDamageable", at = @At("HEAD"), cancellable = true)
    private void mialeeMisc$disableDamageable(CallbackInfoReturnable<Boolean> cir) {
        if (this.isIn(MialeeMisc.UNBREAKABLE)) {
            cir.setReturnValue(false);
        }
    }
}