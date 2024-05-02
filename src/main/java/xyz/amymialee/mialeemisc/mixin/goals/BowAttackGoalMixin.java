package xyz.amymialee.mialeemisc.mixin.goals;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;

@Mixin(BowAttackGoal.class)
public abstract class BowAttackGoalMixin {
    @Unique private LivingEntity entity;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void mialeeMisc$betterActor(HostileEntity actor, double speed, int attackInterval, float range, CallbackInfo ci) {
        if (actor != null) this.entity = actor;
    }

    /*@WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "sh"))
    private boolean mialeeMisc$universalRangedAttack(RangedAttackMob attacker, LivingEntity target, float pullProgress) {
        if (attacker instanceof LivingEntity living && IUniversalRangedItem.tryRangedAttack(living, target, pullProgress))
            return false;
        return true;
    }*/

    @Inject(method = "isHoldingBow", at = @At("HEAD"), cancellable = true)
    private void mialeeMisc$moreBows(CallbackInfoReturnable<Boolean> cir) {
        if (this.entity.isHolding((stack -> stack.getItem() instanceof IUniversalRangedItem))) cir.setReturnValue(true);
    }
}