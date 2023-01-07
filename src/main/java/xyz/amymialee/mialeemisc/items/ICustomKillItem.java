package xyz.amymialee.mialeemisc.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface ICustomKillItem {
    void mialeeMisc$onKilledOther(LivingEntity player, ItemStack stack, LivingEntity other);
}