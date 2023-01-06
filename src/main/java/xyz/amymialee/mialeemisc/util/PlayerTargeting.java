package xyz.amymialee.mialeemisc.util;

import net.minecraft.entity.LivingEntity;

public interface PlayerTargeting {
    LivingEntity mialeeMisc$getLastTarget();
    void mialeeMisc$setLastTarget(LivingEntity target);
}