package xyz.amymialee.mialeemisc.items;

import net.minecraft.entity.LivingEntity;

public interface IUniversalRangedAttackWeapon {
    boolean mialeeMisc$universalRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress);

    static boolean tryRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress) {
        if (attacker.getMainHandStack().getItem() instanceof IUniversalRangedAttackWeapon weapon) {
            if (weapon.mialeeMisc$universalRangedAttack(attacker, target, pullProgress)) return true;
        }
        if (attacker.getOffHandStack().getItem() instanceof IUniversalRangedAttackWeapon weapon) {
            if (weapon.mialeeMisc$universalRangedAttack(attacker, target, pullProgress)) return true;
        }
        return false;
    }
}