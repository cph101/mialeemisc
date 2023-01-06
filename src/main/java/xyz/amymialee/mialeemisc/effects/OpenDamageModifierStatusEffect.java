package xyz.amymialee.mialeemisc.effects;

import net.minecraft.entity.effect.DamageModifierStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class OpenDamageModifierStatusEffect extends DamageModifierStatusEffect {
    public OpenDamageModifierStatusEffect(StatusEffectCategory category, int color, double modifier) {
        super(category, color, modifier);
    }
}