package xyz.amymialee.mialeemisc.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.PresetsScreen;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PresetsScreen.SuperflatPresetsListWidget.class)
public class PresetsScreenMixin {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;iterateEntries(Lnet/minecraft/registry/tag/TagKey;)Ljava/lang/Iterable;"))
    private static Iterable<?> iterateEntries(Registry<FlatLevelGeneratorPreset> registry, TagKey<FlatLevelGeneratorPreset> tagKey, Operation<Iterable<?>> operation) {
        return registry.getIndexedEntries();
    }
}