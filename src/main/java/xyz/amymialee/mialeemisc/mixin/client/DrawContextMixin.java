package xyz.amymialee.mialeemisc.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
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
    @Shadow public abstract void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color);
    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getItemCooldownManager()Lnet/minecraft/entity/player/ItemCooldownManager;"))
    public void mialeeMisc$cooldownOverlay(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        if (clientPlayerEntity != null && stack.getItem() instanceof ICustomCooldownsItem item) {
            Identifier[] array = item.mialeeMisc$getCooldownIdentifiers();
            for (var i = 0; i < array.length; i++) {
                float progress = IdentifierCooldownManager.get(clientPlayerEntity).getCooldownProgress(array[i], MinecraftClient.getInstance().getTickDelta());
                if (progress > 0.0f) {
                    var width = (16f / array.length);
                    var k = y + MathHelper.floor(16.0F * (1.0F - progress));
                    var l = k + MathHelper.ceil(16.0F * progress);
                    this.fill(RenderLayer.getGuiOverlay(), (int) (x + width * i), k, (int) (x + width * (i + 1)), l, Integer.MAX_VALUE);
                }
            }
        }
    }

    @WrapOperation(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;getCooldownProgress(Lnet/minecraft/item/Item;F)F"))
    public float mialeeMisc$cooldownStopper(ItemCooldownManager manager, Item item, float tickDelta, Operation<Float> original) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        if (clientPlayerEntity != null && item instanceof ICustomCooldownsItem) {
            return 0.0f;
        } else return original.call(manager, item, tickDelta);
    }
}
