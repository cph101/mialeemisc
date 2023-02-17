package xyz.amymialee.mialeemisc.mixin.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin extends LivingEntity {
    protected WitherEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "shootSkullAt(ILnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    public void mialeeMisc$universalRangedAttack(int headIndex, LivingEntity target, CallbackInfo ci) {
        if (IUniversalRangedItem.tryRangedAttack(this, target, 1.0f)) ci.cancel();
    }
}