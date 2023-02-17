package xyz.amymialee.mialeemisc.mixin.goals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.IUniversalRangedWeapon;

@Mixin(targets = "net.minecraft.entity.mob.DrownedEntity$TridentAttackGoal")
public class TridentAttackGoalMixin extends ProjectileAttackGoal {
    @Shadow @Final private DrownedEntity drowned;

    public TridentAttackGoalMixin(RangedAttackMob mob, double mobSpeed, int intervalTicks, float maxShootRange) {
        super(mob, mobSpeed, intervalTicks, maxShootRange);
    }

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    public void mialeeMisc$moreTridents(CallbackInfoReturnable<Boolean> cir) {
        if (super.canStart() && this.drowned.isHolding((stack) -> stack.getItem() instanceof IUniversalRangedWeapon)) {
            cir.setReturnValue(true);
        }
    }

    @WrapOperation(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/ProjectileAttackGoal;start()V"))
    public void mialeeMisc$offHanded(DrownedEntity entity, Hand hand, Operation<Void> original) {
        ItemStack stack = entity.getStackInHand(Hand.MAIN_HAND);
        if (!stack.isOf(Items.TRIDENT) && !(stack.getItem() instanceof IUniversalRangedWeapon)) {
            original.call(entity, Hand.OFF_HAND);
        }
        original.call(entity, hand);
    }
}