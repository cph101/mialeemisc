package xyz.amymialee.mialeemisc.mixin.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends LivingEntity {
    protected PiglinEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true)
    public void mialeeMisc$anyoneCanWeapon(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cir) {
        if (weapon instanceof IUniversalRangedItem) {
            cir.setReturnValue(true);
        }
    }
}