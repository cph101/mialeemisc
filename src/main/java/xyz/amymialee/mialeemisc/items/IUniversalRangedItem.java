package xyz.amymialee.mialeemisc.items;

import net.minecraft.entity.LivingEntity;

public interface IUniversalRangedItem {
    boolean mialeeMisc$canRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress);
    void mialeeMisc$universalRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress);

    static boolean canRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress) {
        if (attacker.getMainHandStack().getItem() instanceof IUniversalRangedItem weapon) {
            if (weapon.mialeeMisc$canRangedAttack(attacker, target, pullProgress)) {
                return true;
            }
        }
        if (attacker.getOffHandStack().getItem() instanceof IUniversalRangedItem weapon) {
            return weapon.mialeeMisc$canRangedAttack(attacker, target, pullProgress);
        }
        return false;
    }

    static boolean tryRangedAttack(LivingEntity attacker, LivingEntity target, float pullProgress) {
        if (attacker.getMainHandStack().getItem() instanceof IUniversalRangedItem weapon) {
            if (weapon.mialeeMisc$canRangedAttack(attacker, target, pullProgress)) {
                weapon.mialeeMisc$universalRangedAttack(attacker, target, pullProgress);
                return true;
            }
        }
        if (attacker.getOffHandStack().getItem() instanceof IUniversalRangedItem weapon) {
            if (weapon.mialeeMisc$canRangedAttack(attacker, target, pullProgress)) {
                weapon.mialeeMisc$universalRangedAttack(attacker, target, pullProgress);
                return true;
            }
        }
        return false;
    }
}