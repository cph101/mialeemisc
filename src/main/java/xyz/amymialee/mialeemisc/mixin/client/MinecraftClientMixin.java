package xyz.amymialee.mialeemisc.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.MialeeMisc;
import xyz.amymialee.mialeemisc.entities.IPlayerTargeting;
import xyz.amymialee.mialeemisc.items.IClickConsumingItem;
import xyz.amymialee.mialeemisc.items.ICustomTrackingItem;

import java.util.function.Predicate;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientWorld world;
    @Shadow public abstract @Nullable Entity getCameraEntity();
    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void mialeeMisc$cancelAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null) {
            if (this.player.getMainHandStack().getItem() instanceof IClickConsumingItem) {
                ClientPlayNetworking.send(MialeeMisc.clickConsumePacket, PacketByteBufs.empty());
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;tick(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/hit/HitResult;)V"))
    public void mialeeMisc$setLastTarget(CallbackInfo ci) {
        var camera = this.getCameraEntity();
        if (this.player == null) {
            return;
        }
        if (camera == null) {
            return;
        }
        if (this.world == null) {
            return;
        }
        double distanceCap = 128f * 128f;
        var cameraPos = camera.getCameraPosVec(1.0f);
        var cameraRot = camera.getRotationVec(1.0f);
        var cameraTarget = cameraPos.add(cameraRot.multiply(distanceCap));
        var box = camera.getBoundingBox().stretch(cameraTarget).expand(1.0, 1.0, 1.0);
        Predicate<Entity> predicate = this::mialeeMisc$isValidTarget;
        if (this.player.getMainHandStack().getItem() instanceof ICustomTrackingItem item) {
            predicate = item.mialeeMisc$getTrackingPredicate(this.player);
        }
        var entityHitResult = mialeeMisc$raycast(this.player, box, predicate);
        if (entityHitResult != null) {
            var target = entityHitResult.getEntity();
            if (target instanceof LivingEntity living) {
                if (this.player instanceof IPlayerTargeting targeting) {
                    targeting.mialeeMisc$setLastTarget(living);
                }
            }
        }
    }

    @Unique
    public boolean mialeeMisc$isValidTarget(@Nullable Entity target) {
        if (!(target instanceof LivingEntity living)) return false;
        if (this.player == null) return false;
        if (target == this.player) return false;
        if (!this.player.canSee(target)) return false;
        if (living.isDead()) return false;
        if (target.isRemoved()) return false;
        if (target.isTeammate(this.player)) return false;
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) return false;
        if (target instanceof TameableEntity tamed && tamed.isOwner(this.player)) return false;
        return target.canHit();
    }

    @Unique
    private static EntityHitResult mialeeMisc$raycast(Entity player, Box box, Predicate<Entity> predicate) {
        Entity target = null;
        var targetDistance = 0.01;
        var rotationVec = player.getRotationVector();
        for (var possibleTarget : player.getWorld().getEntitiesByClass(Entity.class, box, predicate)) {
            if (possibleTarget.getRootVehicle() == player.getRootVehicle()) {
                continue;
            }
            var playerDistance = possibleTarget.getPos().add(0, possibleTarget.getHeight() / 2, 0).subtract(player.getEyePos());
            var distance = 1 - playerDistance.normalize().dotProduct(rotationVec);
            if (distance > targetDistance) {
                continue;
            }
            target = possibleTarget;
            targetDistance = distance;
        }
        return target == null ? null : new EntityHitResult(target, target.getPos());
    }
}