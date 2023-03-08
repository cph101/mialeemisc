package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
    @Inject(method = "getHandPossiblyHolding", at = @At("RETURN"), cancellable = true)
    private static void mialeeMisc$universalItems(LivingEntity entity, Item item, CallbackInfoReturnable<Hand> cir) {
        if (cir.getReturnValue() == Hand.OFF_HAND) {
            if (entity.getMainHandStack().getItem() instanceof IUniversalRangedItem) {
                cir.setReturnValue(Hand.MAIN_HAND);
            }
        }
    }
}