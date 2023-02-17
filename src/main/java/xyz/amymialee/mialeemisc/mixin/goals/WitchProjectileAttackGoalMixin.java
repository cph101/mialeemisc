package xyz.amymialee.mialeemisc.mixin.goals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.WitchEntity;
import org.spongepowered.asm.mixin.Mixin;
import xyz.amymialee.mialeemisc.entities.goals.LlamaProjectileAttackGoal;

@Mixin(LlamaProjectileAttackGoal.class)
public class WitchProjectileAttackGoalMixin extends ProjectileAttackGoalMixin {
    @Override
    protected void mialeeMisc$universalRangedAttack(RangedAttackMob owner, LivingEntity target, float pullProgress, Operation<Void> original) {
        if (owner instanceof WitchEntity witch) {
            if (witch.isDrinking()) return;
        }
        super.mialeeMisc$universalRangedAttack(owner, target, pullProgress, original);
    }
}