package xyz.amymialee.mialeemisc.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
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

    @Shadow @Final private static Identifier[] TAB_TOP_SELECTED_TEXTURES;

    @Shadow @Final private static Identifier[] TAB_BOTTOM_SELECTED_TEXTURES;

    @Shadow @Final private static Identifier[] TAB_BOTTOM_UNSELECTED_TEXTURES;

    @Shadow @Final private static Identifier[] TAB_TOP_UNSELECTED_TEXTURES;

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "renderTabIcon", at = @At(value = "HEAD"), cancellable = true)
    protected void mialeeMisc$creativeTabIcon(DrawContext context, ItemGroup group, CallbackInfo ci) {
        if (group instanceof MialeeItemGroup mialeeItemGroup) {
            boolean onTop = group.getRow() == ItemGroup.Row.TOP;
            boolean isSelected = group == selectedTab;
            int v = group.getColumn();
            int x = this.x + this.getTabX(group);
            int y = this.y - (onTop ? 28 : -(this.backgroundHeight - 4));
            Identifier[] identifiers;
            if (onTop) {
                identifiers = isSelected ? TAB_TOP_SELECTED_TEXTURES : TAB_TOP_UNSELECTED_TEXTURES;
            } else {
                identifiers = isSelected ? TAB_BOTTOM_SELECTED_TEXTURES : TAB_BOTTOM_UNSELECTED_TEXTURES;
            }
            context.drawGuiTexture(identifiers[MathHelper.clamp(v, 0, identifiers.length)], x, y, 26, 32);
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, 0.0F, 100.0F);

            x += 5;
            y += 8 + (onTop ? 1 : -1);
            int cycle = group.hashCode();
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                cycle += player.age;
            }
            ItemStack itemStack = mialeeItemGroup.createIcon(cycle);
            context.drawItem(itemStack, this.x, this.y);
            context.drawItemInSlot(this.textRenderer, itemStack, this.x, this.y);
            //context.off = 0.0F;
            ci.cancel();
        }
    }
}