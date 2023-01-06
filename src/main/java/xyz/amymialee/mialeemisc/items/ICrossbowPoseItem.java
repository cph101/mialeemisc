package xyz.amymialee.mialeemisc.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public interface ICrossbowPoseItem {
    boolean mialeeMisc$shouldHoldCrossbowHold(LivingEntity entity, Hand hand);
    boolean mialeeMisc$shouldHoldCrossbowCharge(LivingEntity entity, Hand hand);
}