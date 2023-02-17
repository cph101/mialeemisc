package xyz.amymialee.mialeemisc.mixin.mobs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.mialeemisc.entities.goals.LlamaProjectileAttackGoal;

@Mixin(LlamaEntity.class)
public abstract class LLamaEntityMixin extends LivingEntity {
    protected LLamaEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @WrapOperation(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 3))
    public void mialeeMisc$llamaGoal(GoalSelector goalSelector, int i, Goal goal, Operation<Void> original) {
        if (goal instanceof ProjectileAttackGoal attackGoal) {
            original.call(goalSelector, i, new LlamaProjectileAttackGoal(attackGoal));
        } else {
            original.call(goalSelector, i, goal);
        }
    }
}