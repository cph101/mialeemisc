package xyz.amymialee.mialeemisc.mixin.goals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.mialeemisc.items.IUniversalRangedAttackWeapon;

@Mixin(CrossbowAttackGoal.class)
public class CrossbowAttackGoalMixin<T extends HostileEntity & RangedAttackMob & CrossbowUser> {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/RangedAttackMob;attack(Lnet/minecraft/entity/LivingEntity;F)V"))
    private void mialeeMisc$universalRangedAttack(T attacker, LivingEntity target, float pullProgress, Operation<Void> original) {
        if (IUniversalRangedAttackWeapon.tryRangedAttack(attacker, target, pullProgress)) return;
        original.call(attacker, target, pullProgress);
    }
}