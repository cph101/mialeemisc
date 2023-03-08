package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.mialeemisc.entities.IExtraData;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "updatePotionVisibility", at = @At("HEAD"), cancellable = true)
    private void mialeeMisc$updateVisibility(CallbackInfo ci) {
        if (((Object) this) instanceof IExtraData extraData) {
            extraData.mialeeMisc$updateImperceptible();
        }
    }
}