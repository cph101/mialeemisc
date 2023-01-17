package xyz.amymialee.mialeemisc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
    @Shadow private int pickupDelay;

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void mialeeMisc$undestroyable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getStack().isIn(MialeeMisc.DAMAGE_IMMUNE)) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V", ordinal = 1))
    private void mialeeMisc$youth(ItemEntity entity, Operation<Void> original) {
        if (!this.getStack().isIn(MialeeMisc.DAMAGE_IMMUNE) || this.pickupDelay == 32767) {
            original.call(entity);
        }
    }
}