package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.entities.IExtraData;
import xyz.amymialee.mialeemisc.events.ImperceptibleCallback;
import xyz.amymialee.mialeemisc.items.ICustomKillItem;

@Mixin(Entity.class)
public class EntityMixin implements IExtraData {
    @Shadow @Final protected DataTracker dataTracker;

    @Unique private static final TrackedData<Boolean> IMPERCEPTIBLE = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "onKilledOther", at = @At("TAIL"))
    private void mialeeMisc$customKills(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        var this2 = (Entity) (Object) this;
        if (this2 instanceof LivingEntity living) {
            var main = living.getMainHandStack();
            if (main.getItem() instanceof ICustomKillItem item) {
                item.mialeeMisc$onKilledOther(living, main, other);
            }
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void mialeeMisc$extraData(CallbackInfo ci) {
        this.dataTracker.startTracking(IMPERCEPTIBLE, false);
    }

    @Override
    public boolean mialeeMisc$isImperceptible() {
        return this.dataTracker.get(IMPERCEPTIBLE);
    }

    @Override
    public void mialeeMisc$updateImperceptible() {
        var result = ImperceptibleCallback.EVENT.invoker().interact((Entity) (Object) this);
        if (result == ActionResult.SUCCESS) {
            this.dataTracker.set(IMPERCEPTIBLE, true);
        } else if (result == ActionResult.FAIL) {
            this.dataTracker.set(IMPERCEPTIBLE, false);
        }
    }
}