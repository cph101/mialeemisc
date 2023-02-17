package xyz.amymialee.mialeemisc.entities.goals;

import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import xyz.amymialee.mialeemisc.mixin.accessors.ProjectileAttackGoalAccessor;

public class LlamaProjectileAttackGoal extends ProjectileAttackGoal {
    public LlamaProjectileAttackGoal(ProjectileAttackGoal goal) {
        super(((ProjectileAttackGoalAccessor) goal).getOwner(), ((ProjectileAttackGoalAccessor) goal).getMobSpeed(), ((ProjectileAttackGoalAccessor) goal).getMinIntervalTicks(), ((ProjectileAttackGoalAccessor) goal).getMaxIntervalTicks(), ((ProjectileAttackGoalAccessor) goal).getMaxShootRange());
    }
}