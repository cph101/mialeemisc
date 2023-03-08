package xyz.amymialee.mialeemisc.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/client/gui/screen/PresetsScreen$SuperflatPresetsListWidget")
public class PresetsScreenMixin {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/Registry;iterateEntries(Lnet/minecraft/tag/TagKey;)Ljava/lang/Iterable;"))
    private static Iterable<?> iterateEntries(Registry<FlatLevelGeneratorPreset> registry, TagKey<FlatLevelGeneratorPreset> tagKey, Operation<Iterable<?>> operation) {
        return registry.getIndexedEntries();
    }
}