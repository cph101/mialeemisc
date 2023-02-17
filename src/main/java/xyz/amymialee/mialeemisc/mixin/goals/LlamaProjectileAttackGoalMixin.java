package xyz.amymialee.mialeemisc.mixin.goals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import org.spongepowered.asm.mixin.Mixin;
import xyz.amymialee.mialeemisc.entities.goals.LlamaProjectileAttackGoal;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;
import xyz.amymialee.mialeemisc.mixin.accessors.LlamaEntityAccessor;

@Mixin(LlamaProjectileAttackGoal.class)
public class LlamaProjectileAttackGoalMixin extends ProjectileAttackGoalMixin {
    @Override
    protected void mialeeMisc$universalRangedAttack(RangedAttackMob owner, LivingEntity target, float pullProgress, Operation<Void> original) {
        if (owner instanceof LivingEntity living && IUniversalRangedItem.canRangedAttack(living, target, pullProgress)) {
            if (owner instanceof LlamaEntityAccessor llamaEntity) {
                llamaEntity.mialeeMisc$setSpit(true);
            }
        }
        super.mialeeMisc$universalRangedAttack(owner, target, pullProgress, original);
    }
}