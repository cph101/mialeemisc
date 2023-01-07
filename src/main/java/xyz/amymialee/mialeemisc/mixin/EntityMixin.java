package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.items.ICustomKillItem;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "onKilledOther", at = @At("TAIL"))
    private void mialeeMisc$customKills(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        Entity this2 = (Entity) (Object) this;
        if (this2 instanceof LivingEntity living) {
            ItemStack main = living.getMainHandStack();
            if (main.getItem() instanceof ICustomKillItem item) {
                item.mialeeMisc$onKilledOther(living, main, other);
            }
        }
    }
}