package xyz.amymialee.mialeemisc.mixin;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;
import net.minecraft.world.gen.FlatLevelGeneratorPresets;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.MialeeMisc;
import xyz.amymialee.mialeemisc.MialeeMiscConfig;
import xyz.amymialee.mialeemisc.flatpreset.FlatPresetRegistry;

import java.util.Map;
import java.util.Set;

@Mixin(FlatLevelGeneratorPresets.class)
public class FlatLevelGeneratorPresetsMixin {
    @Mixin(FlatLevelGeneratorPresets.Registrar.class)
    static abstract class RegistrarMixin {
        @Shadow public abstract RegistryEntry<FlatLevelGeneratorPreset> createAndRegister(RegistryKey<FlatLevelGeneratorPreset> registryKey, ItemConvertible icon, RegistryKey<Biome> biome, Set<RegistryKey<StructureSet>> structureSets, boolean hasFeatures, boolean hasLakes, FlatChunkGeneratorLayer... layers);

        @Inject(method = "initAndGetDefault", at = @At("TAIL"))
        private void mialeeMisc$addPresets(CallbackInfoReturnable<String> cir) {
            FlatPresetRegistry.addEntry(new FlatPresetRegistry.FlatPresetEntry(
                    MialeeMisc.id("dev_ready"),
                    Items.RED_SANDSTONE,
                    BiomeKeys.DARK_FOREST,
                    ImmutableSet.of(),
                    false,
                    false,
                    new FlatChunkGeneratorLayer(64, Blocks.RED_SANDSTONE),
                    new FlatChunkGeneratorLayer(63, Blocks.DEEPSLATE),
                    new FlatChunkGeneratorLayer(1, Blocks.BEDROCK)
            ));
//            for (Map.Entry<Identifier, FlatChunkGeneratorConfig> entry : MialeeMiscConfig.flatPresets.entrySet()) {
//                Identifier id = entry.getKey();
//                FlatChunkGeneratorConfig config = entry.getValue();
//                if (config.getStructureOverrides().isPresent()) FlatPresetRegistry.addEntry(new FlatPresetRegistry.FlatPresetEntry(
//                        id,
//                        Items.COMMAND_BLOCK,
//                        config.getBiome(),
//                        config.getStructureOverrides().get(),
//                        false,
//                        false,
//                        config.getLayers().toArray(new FlatChunkGeneratorLayer[0])
//                ));
//            }
            for (FlatPresetRegistry.FlatPresetEntry entry : FlatPresetRegistry.getEntries()) {
                RegistryKey<FlatLevelGeneratorPreset> registryKey = RegistryKey.of(Registry.FLAT_LEVEL_GENERATOR_PRESET_KEY, entry.identifier());
                this.createAndRegister(registryKey, entry.icon(), entry.biome(), entry.structureSets(), entry.hasFeatures(), entry.hasLakes(), entry.layers());
            }
        }
    }
}