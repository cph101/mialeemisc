package xyz.amymialee.mialeemisc.entities.goals;

import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import xyz.amymialee.mialeemisc.mixin.accessors.ProjectileAttackGoalAccessor;

public class WitchProjectileAttackGoal extends ProjectileAttackGoal {
    public WitchProjectileAttackGoal(ProjectileAttackGoal goal) {
        super(((ProjectileAttackGoalAccessor) goal).getOwner(), ((ProjectileAttackGoalAccessor) goal).getMobSpeed(), ((ProjectileAttackGoalAccessor) goal).getMinIntervalTicks(), ((ProjectileAttackGoalAccessor) goal).getMaxIntervalTicks(), ((ProjectileAttackGoalAccessor) goal).getMaxShootRange());
    }
}