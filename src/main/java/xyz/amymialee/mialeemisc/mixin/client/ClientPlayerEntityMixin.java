package xyz.amymialee.mialeemisc.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.mialeemisc.MialeeMisc;
import xyz.amymialee.mialeemisc.entities.IPlayerTargeting;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements IPlayerTargeting {
    @Unique @Nullable LivingEntity lastTarget;
    @Unique int targetDecayTime;

    @Override
    public LivingEntity mialeeMisc$getLastTarget() {
        return this.lastTarget;
    }

    @Override
    public void mialeeMisc$setLastTarget(LivingEntity target) {
        this.lastTarget = target;
        this.targetDecayTime = 60;
        if (target != null) {
            var buf = PacketByteBufs.create();
            buf.writeInt(target.getId());
            ClientPlayNetworking.send(MialeeMisc.targetPacket, buf);
        }
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
}