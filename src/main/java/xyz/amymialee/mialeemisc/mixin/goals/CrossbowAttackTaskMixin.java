package xyz.amymialee.mialeemisc.mixin.goals;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;

@Mixin(CrossbowAttackTask.class)
public class CrossbowAttackTaskMixin<E extends MobEntity & CrossbowUser> {
    @Shadow private CrossbowAttackTask.CrossbowState state;

    @Inject(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/RangedAttackMob;attack(Lnet/minecraft/entity/LivingEntity;F)V"))
    private void mialeeMisc$universalRangedAttack(E entity, LivingEntity target, CallbackInfo ci) {
        if (IUniversalRangedItem.tryRangedAttack(entity, target, 1.0f)) {
            this.state = CrossbowAttackTask.CrossbowState.UNCHARGED;
            ci.cancel();
        }
    }
}