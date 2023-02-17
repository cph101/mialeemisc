package xyz.amymialee.mialeemisc.mixin.mobs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.mialeemisc.items.IUniversalRangedItem;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity {
    @Shadow @Final private MeleeAttackGoal meleeAttackGoal;
    @Shadow @Final private BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal;

    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @WrapOperation(method = "updateAttackType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    public boolean mialeeMisc$universalAttackType(ItemStack stack, Item item, Operation<Boolean> original) {
        if (original.call(stack, item)) {
            return true;
        }
        if (stack.getItem() instanceof IUniversalRangedItem) {
            return true;
        }
        return this.getMainHandStack().getItem() instanceof IUniversalRangedItem;
    }
}