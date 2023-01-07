package xyz.amymialee.mialeemisc.items;

import net.minecraft.entity.Entity;

import java.util.function.Predicate;

public interface ICustomTrackingItem {
    Predicate<Entity> mialeeMisc$getTrackingPredicate();
}