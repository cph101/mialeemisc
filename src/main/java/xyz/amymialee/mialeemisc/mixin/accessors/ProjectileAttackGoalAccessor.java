package xyz.amymialee.mialeemisc.mixin.accessors;

import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ProjectileAttackGoal.class)
public interface ProjectileAttackGoalAccessor {
    @Accessor("owner")
    RangedAttackMob getOwner();
    @Accessor("mobSpeed")
    double getMobSpeed();
    @Accessor("minIntervalTicks")
    int getMinIntervalTicks();
    @Accessor("maxIntervalTicks")
    int getMaxIntervalTicks();
    @Accessor("maxShootRange")
    float getMaxShootRange();
}