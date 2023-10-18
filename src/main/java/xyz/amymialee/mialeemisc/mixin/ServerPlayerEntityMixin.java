package xyz.amymialee.mialeemisc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.mialeemisc.cooldowns.IdentifierCooldownManager;
import xyz.amymialee.mialeemisc.cooldowns.ServerIdentifierCooldownManager;
import xyz.amymialee.mialeemisc.entities.IPlayerTargeting;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements IPlayerTargeting {
    @Unique @Nullable LivingEntity lastTarget;
    @Unique int targetDecayTime;

    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected IdentifierCooldownManager createIdentifierCooldownManager() {
        return new ServerIdentifierCooldownManager((ServerPlayerEntity) (Object) this);
    }

    @Override
    public LivingEntity mialeeMisc$getLastTarget() {
        return this.lastTarget;
    }

    @Override
    public void mialeeMisc$setLastTarget(LivingEntity target) {
        this.lastTarget = target;
        this.targetDecayTime = 60;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void mialeeMisc$decayTarget(CallbackInfo ci) {
        if (this.targetDecayTime > 0) {
            this.targetDecayTime--;
            if (this.targetDecayTime == 0) {
                this.mialeeMisc$setLastTarget(null);
            }
        }
    }

    @WrapOperation(method = "playerTick", at = @At(value = "INVOKE" , target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item mialeeMisc$nullCheck(ItemStack stack, Operation<Item> original) {
        var originalItem = original.call(stack);
        if (originalItem == null) {
            return Items.AIR;
        }
        return originalItem;
    }
}