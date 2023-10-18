package xyz.amymialee.mialeemisc.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.mialeemisc.itemgroup.MialeeItemGroup;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    @Shadow private static ItemGroup selectedTab;
    @Shadow protected abstract int getTabX(ItemGroup group);

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "renderTabIcon", at = @At(value = "HEAD"), cancellable = true)
    protected void mialeeMisc$creativeTabIcon(MatrixStack matrices, ItemGroup group, CallbackInfo ci) {
        if (group instanceof MialeeItemGroup mialeeItemGroup) {
            var onTop = group.getRow() == ItemGroup.Row.TOP;
            var v = 0;
            var x = this.x + this.getTabX(group);
            var y = this.y;
            if (group == selectedTab) {
                v += 32;
            }
            if (onTop) {
                y -= 28;
            } else {
                v += 64;
                y += this.backgroundHeight - 4;
            }
            this.drawTexture(matrices, x, y, group.getColumn() * 26, v, 26, 32);
            this.itemRenderer.zOffset = 100.0F;
            x += 5;
            y += 8 + (onTop ? 1 : -1);
            var cycle = group.hashCode();
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                cycle += player.age;
            }
            var itemStack = mialeeItemGroup.createIcon(cycle);
            this.itemRenderer.renderInGuiWithOverrides(itemStack, x, y);
            this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack, x, y);
            this.itemRenderer.zOffset = 0.0F;
            ci.cancel();
        }
    }
}