package xyz.amymialee.mialeemisc.mixin.goals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;

@Mixin(CrossbowAttackGoal.class)
public class CrossbowAttackGoalMixin {
    @Unique private LivingEntity entity;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void mialeeMisc$betterActor(HostileEntity actor, double speed, float range, CallbackInfo ci) {
        if (actor != null) this.entity = actor;
    }

    /*@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/RangedAttackMob;shootAt(Lnet/minecraft/entity/LivingEntity;F)V"))
    private void mialeeMisc$universalRangedAttack(RangedAttackMob attacker, LivingEntity target, float pullProgress, Operation<Void> original) {
        if (attacker instanceof LivingEntity living && IUniversalRangedItem.tryRangedAttack(living, target, pullProgress)) return;
        original.call(attacker, target, pullProgress);
    }*/

    @Inject(method = "isEntityHoldingCrossbow", at = @At("HEAD"), cancellable = true)
    private void mialeeMisc$moreCrossbows(CallbackInfoReturnable<Boolean> cir) {
        if (this.entity.isHolding((stack -> stack.getItem() instanceof IUniversalRangedItem))) cir.setReturnValue(true);
    }
}