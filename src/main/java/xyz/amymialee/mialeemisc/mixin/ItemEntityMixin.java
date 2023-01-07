package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.MialeeMisc;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getStack();

    @Shadow private int itemAge;

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void mialeeMisc$undestroyable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getStack().isIn(MialeeMisc.DAMAGE_IMMUNE)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void mialeeMisc$youth(CallbackInfo ci) {
        if (this.getStack().isIn(MialeeMisc.DAMAGE_IMMUNE)) {
            this.itemAge--;
        }
    }
}