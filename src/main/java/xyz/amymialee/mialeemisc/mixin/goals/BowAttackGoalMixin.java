package xyz.amymialee.mialeemisc.mixin.goals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.IUniversalRangedWeapon;

@Mixin(BowAttackGoal.class)
public class BowAttackGoalMixin<T extends HostileEntity & RangedAttackMob> {
    @Shadow @Final private T actor;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/RangedAttackMob;attack(Lnet/minecraft/entity/LivingEntity;F)V"))
    private void mialeeMisc$universalRangedAttack(T attacker, LivingEntity target, float pullProgress, Operation<Void> original) {
        if (IUniversalRangedWeapon.tryRangedAttack(attacker, target, pullProgress)) return;
        original.call(attacker, target, pullProgress);
    }

    @Inject(method = "isHoldingBow", at = @At("HEAD"), cancellable = true)
    private void mialeeMisc$moreBows(CallbackInfoReturnable<Boolean> cir) {
        if (this.actor.isHolding((stack -> stack.getItem() instanceof IUniversalRangedWeapon))) cir.setReturnValue(true);
    }
}