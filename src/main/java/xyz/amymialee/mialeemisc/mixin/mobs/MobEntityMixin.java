package xyz.amymialee.mialeemisc.mixin.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.ICustomAttackItem;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Override @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tryAttack", at = @At("HEAD"), cancellable = true)
    public void mialeeMisc$customAttacks(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (!target.isAttackable()) {
            return;
        }
        if (target instanceof EnderDragonPart) {
            target = ((EnderDragonPart)target).owner;
        }
        var main = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (main.getItem() instanceof ICustomAttackItem item) {
            if (item.mialeeMisc$customAttack(this, target)) {
                cir.setReturnValue(true);
            }
        }
    }
}