package xyz.amymialee.mialeemisc.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.MialeeMisc;
import xyz.amymialee.mialeemisc.items.ICustomCooldownsItem;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow protected abstract void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);
    @Shadow @Final private ItemModels models;

    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"))
    public void mialeeMisc$cooldownOverlay(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        if (clientPlayerEntity != null && stack.getItem() instanceof ICustomCooldownsItem item) {
            Item[] array = item.mialeeMisc$getCooldownItems();
            for (int i = 0; i < array.length; i++) {
                float progress = clientPlayerEntity.getItemCooldownManager().getCooldownProgress(array[i], MinecraftClient.getInstance().getTickDelta());
                if (progress > 0.0f) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.disableTexture();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    Tessellator tessellator2 = Tessellator.getInstance();
                    BufferBuilder bufferBuilder2 = tessellator2.getBuffer();
                    this.renderGuiQuad(bufferBuilder2, x + MathHelper.ceil(16f / array.length) * i, y + MathHelper.floor(16.0f * (1.0f - progress)), MathHelper.ceil(16f / array.length), MathHelper.ceil(16.0f * progress), 255, 255, 255, 127);
                    RenderSystem.enableTexture();
                    RenderSystem.enableDepthTest();
                }
            }
        }
    }

    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;getCooldownProgress(Lnet/minecraft/item/Item;F)F"), cancellable = true)
    public void mialeeMisc$cooldownStopper(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        if (clientPlayerEntity != null && stack.getItem() instanceof ICustomCooldownsItem) {
            ci.cancel();
        }
    }

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    private void legends$heldItemModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (MialeeMisc.INVENTORY_ITEMS.contains(stack.getItem())) {
            BakedModel bakedModel = this.models.getModelManager().getModel(new ModelIdentifier("minecraft:trident_in_hand#inventory"));
            ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;
            BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
            cir.setReturnValue(bakedModel2 == null ? this.models.getModelManager().getMissingModel() : bakedModel2);
        }
    }
}