package xyz.amymialee.mialeemisc.items;

import net.minecraft.entity.LivingEntity;

public interface IUniversalRangedWeapon {
    boolean mialeeMisc$canRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress);
    void mialeeMisc$universalRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress);

    static boolean canRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress) {
        if (attacker.getMainHandStack().getItem() instanceof IUniversalRangedWeapon weapon) {
            if (weapon.mialeeMisc$canRangedAttack(attacker, target, pullProgress)) {
                return true;
            }
        }
        if (attacker.getOffHandStack().getItem() instanceof IUniversalRangedWeapon weapon) {
            return weapon.mialeeMisc$canRangedAttack(attacker, target, pullProgress);
        }
        return false;
    }

    static boolean tryRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress) {
        if (attacker.getMainHandStack().getItem() instanceof IUniversalRangedWeapon weapon) {
            if (weapon.mialeeMisc$canRangedAttack(attacker, target, pullProgress)) {
                weapon.mialeeMisc$universalRangedAttack(attacker, target, pullProgress);
                return true;
            }
        }
        if (attacker.getOffHandStack().getItem() instanceof IUniversalRangedWeapon weapon) {
            if (weapon.mialeeMisc$canRangedAttack(attacker, target, pullProgress)) {
                weapon.mialeeMisc$universalRangedAttack(attacker, target, pullProgress);
                return true;
            }
        }
        return false;
    }
}