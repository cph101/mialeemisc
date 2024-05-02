package xyz.amymialee.mialeemisc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import org.spongepowered.asm.mixin.injection.Redirect;
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
        Entity this2 = (Entity) (Object) this;
        if (this2 instanceof LivingEntity living) {
            ItemStack main = living.getMainHandStack();
            if (main.getItem() instanceof ICustomKillItem item) {
                item.mialeeMisc$onKilledOther(living, main, other);
            }
        }
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker$Builder;add(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;)Lnet/minecraft/entity/data/DataTracker$Builder;", ordinal = 7))
    private <T> DataTracker.Builder mialeeMisc$extraData(DataTracker.Builder builder, TrackedData<T> data, T value) {
        builder.add(data, value);
        builder.add(IMPERCEPTIBLE, false);
        return builder;
    }

    @Override
    public boolean mialeeMisc$isImperceptible() {
        return this.dataTracker.get(IMPERCEPTIBLE);
    }

    @Override
    public void mialeeMisc$updateImperceptible() {
        ActionResult result = ImperceptibleCallback.EVENT.invoker().interact((Entity) (Object) this);
        if (result == ActionResult.SUCCESS) {
            this.dataTracker.set(IMPERCEPTIBLE, true);
        } else if (result == ActionResult.FAIL) {
            this.dataTracker.set(IMPERCEPTIBLE, false);
        }
    }
}