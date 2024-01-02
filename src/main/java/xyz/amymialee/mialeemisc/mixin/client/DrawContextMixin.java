package xyz.amymialee.mialeemisc.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.mialeemisc.cooldowns.IdentifierCooldownManager;
import xyz.amymialee.mialeemisc.items.ICustomCooldownsItem;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Shadow public abstract void fill(int x1, int y1, int x2, int y2, int color);

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"))
    public void mialeeMisc$cooldownOverlay(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        var clientPlayerEntity = MinecraftClient.getInstance().player;
        if (clientPlayerEntity != null && stack.getItem() instanceof ICustomCooldownsItem item) {
            var array = item.mialeeMisc$getCooldownIdentifiers();
            for (var i = 0; i < array.length; i++) {
                var progress = IdentifierCooldownManager.get(clientPlayerEntity).getCooldownProgress(array[i], MinecraftClient.getInstance().getTickDelta());
                if (progress > 0.0f) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    this.fill(x + MathHelper.ceil(16f / array.length) * i, y + MathHelper.floor(16.0f * (1.0f - progress)), MathHelper.ceil(16f / array.length), MathHelper.ceil(16.0f * progress), ColorHelper.Argb.getArgb(127, 255, 255, 255));
                    RenderSystem.enableDepthTest();
                }
            }
        }
    }

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;getCooldownProgress(Lnet/minecraft/item/Item;F)F"), cancellable = true)
    public void mialeeMisc$cooldownStopper(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        var clientPlayerEntity = MinecraftClient.getInstance().player;
        if (clientPlayerEntity != null && stack.getItem() instanceof ICustomCooldownsItem) {
            ci.cancel();
        }
    }
}
