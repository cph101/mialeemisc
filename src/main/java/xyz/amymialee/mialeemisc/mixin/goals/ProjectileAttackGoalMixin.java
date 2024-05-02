package xyz.amymialee.mialeemisc.mixin.goals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;

@Mixin(ProjectileAttackGoal.class)
public class ProjectileAttackGoalMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/RangedAttackMob;shootAt(Lnet/minecraft/entity/LivingEntity;F)V"))
    protected void mialeeMisc$universalRangedAttack(RangedAttackMob attacker, LivingEntity target, float pullProgress, Operation<Void> original) {
        if (attacker instanceof LivingEntity living && IUniversalRangedItem.tryRangedAttack(living, target, pullProgress)) return;
        original.call(attacker, target, pullProgress);
    }
}