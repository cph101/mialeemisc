package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.cooldowns.IdentifierCooldownHolder;
import xyz.amymialee.mialeemisc.events.AutoSmeltingCallback;
import xyz.amymialee.mialeemisc.items.ICustomAttackItem;
import xyz.amymialee.mialeemisc.items.ICustomKillItem;
import xyz.amymialee.mialeemisc.cooldowns.IdentifierCooldownManager;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IdentifierCooldownHolder {
    @Unique private final IdentifierCooldownManager identifierCooldownManager = this.createIdentifierCooldownManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    protected IdentifierCooldownManager createIdentifierCooldownManager() {
        return new IdentifierCooldownManager();
    }

    @Override @Unique
    public IdentifierCooldownManager getIdentifierCooldownManager() {
        return this.identifierCooldownManager;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void mialeeMisc$onTick(CallbackInfo ci) {
        this.identifierCooldownManager.update();
    }

    @Inject(method = "onKilledOther", at = @At("TAIL"))
    private void mialeeMisc$customKills(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        ItemStack main = this.getMainHandStack();
        if (main.getItem() instanceof ICustomKillItem item) {
            item.mialeeMisc$onKilledOther(this, main, other);
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void mialeeMisc$customAttacks(Entity target, CallbackInfo ci) {
        if (!target.isAttackable()) {
            return;
        }
        if (target.handleAttack(this)) {
            return;
        }
        if (target instanceof EnderDragonPart part) {
            target = part.owner;
        }
        ItemStack main = this.getMainHandStack();
        if (main.getItem() instanceof ICustomAttackItem item) {
            if (item.mialeeMisc$customAttack(this, target)) {
                ci.cancel();
            }
        }
    }
}