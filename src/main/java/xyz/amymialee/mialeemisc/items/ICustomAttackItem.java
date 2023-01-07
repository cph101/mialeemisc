package xyz.amymialee.mialeemisc.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public interface ICustomAttackItem {
    boolean mialeeMisc$customAttack(LivingEntity attacker, Entity target);
}