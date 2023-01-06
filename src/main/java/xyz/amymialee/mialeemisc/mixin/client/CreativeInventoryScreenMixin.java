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
    @Shadow private static int selectedTab;

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "renderTabIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;isTopRow()Z"), cancellable = true)
    protected void legends$creativeTabIcon(MatrixStack matrices, ItemGroup group, CallbackInfo ci) {
        if (group instanceof MialeeItemGroup mialeeItemGroup) {
            boolean isSelected = group.getIndex() == selectedTab;
            boolean isTopRow = group.isTopRow();
            int column = group.getColumn();
            int textureX = 0;
            int groupX = this.x + 28 * column;
            int groupY = this.y;
            if (isSelected) {
                textureX += 32;
            }
            if (group.isSpecial()) {
                groupX = this.x + this.backgroundWidth - 28 * (6 - column);
            } else if (column > 0) {
                groupX += column;
            }
            if (isTopRow) {
                groupY -= 28;
            } else {
                textureX += 64;
                groupY += this.backgroundHeight - 4;
            }
            this.drawTexture(matrices, groupX, groupY, column * 28, textureX, 28, 32);
            this.itemRenderer.zOffset = 90.0f;
            int cycle = group.hashCode();
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                cycle += player.age;
            }
            ItemStack stack = mialeeItemGroup.createIcon(cycle);
            int topOffset = isTopRow ? 1 : -1;
            this.itemRenderer.renderInGuiWithOverrides(stack, groupX += 6, groupY += 8 + topOffset);
            this.itemRenderer.renderGuiItemOverlay(this.textRenderer, stack, groupX, groupY);
            this.itemRenderer.zOffset = 0.0f;
            ci.cancel();
        }
    }
}