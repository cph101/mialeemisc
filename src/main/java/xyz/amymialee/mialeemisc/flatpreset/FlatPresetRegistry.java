package xyz.amymialee.mialeemisc.flatpreset;

import net.minecraft.item.ItemConvertible;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatPresetRegistry {
    private static final Set<FlatPresetEntry> entries = new HashSet<>();

    public static void addEntry(FlatPresetEntry entry) {
        entries.add(entry);
    }

    public static Set<FlatPresetEntry> getEntries() {
        return entries;
    }

    private static Set<File> listPossiblePresets(String dir) {
        File[] files = new File(dir).listFiles();
        if (files == null) return Set.of();
        return Stream.of(files).filter(file -> !file.isDirectory()).collect(Collectors.toSet());
    }

    public static final class FlatPresetEntry {
        private final Identifier identifier;
        private final ItemConvertible icon;
        private final RegistryKey<Biome> biome;
        private final Set<RegistryKey<StructureSet>> structureSets;
        private final boolean hasFeatures;
        private final boolean hasLakes;
        private final FlatChunkGeneratorLayer[] layers;

        public FlatPresetEntry(Identifier identifier, ItemConvertible icon, RegistryKey<Biome> biome, Set<RegistryKey<StructureSet>> structureSets, boolean hasFeatures, boolean hasLakes, FlatChunkGeneratorLayer... layers) {
            this.identifier = identifier;
            this.icon = icon;
            this.biome = biome;
            this.structureSets = structureSets;
            this.hasFeatures = hasFeatures;
            this.hasLakes = hasLakes;
            this.layers = layers;
        }

        public FlatPresetEntry(Identifier identifier, ItemConvertible icon, RegistryEntry<Biome> biome, RegistryEntryList<StructureSet> structureSets, boolean hasFeatures, boolean hasLakes, FlatChunkGeneratorLayer... layers) {
            this.identifier = identifier;
            this.icon = icon;
            this.biome = biome.getKey().orElse(BiomeKeys.PLAINS);
            this.structureSets = new HashSet<>();
            for (RegistryEntry<StructureSet> structure : structureSets) {
                if (structure.getKey().isPresent()) {
                    this.structureSets.add(structure.getKey().get());
                }
            }
            this.hasFeatures = hasFeatures;
            this.hasLakes = hasLakes;
            this.layers = layers;
        }

        public Identifier identifier() {
            return this.identifier;
        }

        public ItemConvertible icon() {
            return this.icon;
        }

        public RegistryKey<Biome> biome() {
            return this.biome;
        }

        public Set<RegistryKey<StructureSet>> structureSets() {
            return this.structureSets;
        }

        public boolean hasFeatures() {
            return this.hasFeatures;
        }

        public boolean hasLakes() {
            return this.hasLakes;
        }

        public FlatChunkGeneratorLayer[] layers() {
            return this.layers;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            var that = (FlatPresetEntry) obj;
            return Objects.equals(this.identifier, that.identifier) &&
                    Objects.equals(this.icon, that.icon) &&
                    Objects.equals(this.biome, that.biome) &&
                    Objects.equals(this.structureSets, that.structureSets) &&
                    this.hasFeatures == that.hasFeatures &&
                    this.hasLakes == that.hasLakes &&
                    Arrays.equals(this.layers, that.layers);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.identifier, this.icon, this.biome, this.structureSets, this.hasFeatures, this.hasLakes, Arrays.hashCode(this.layers));
        }

        @Override
        public String toString() {
            return "FlatPresetEntry[" +
                    "identifier=" + this.identifier + ", " +
                    "icon=" + this.icon + ", " +
                    "biome=" + this.biome + ", " +
                    "structureSets=" + this.structureSets + ", " +
                    "hasFeatures=" + this.hasFeatures + ", " +
                    "hasLakes=" + this.hasLakes + ", " +
                    "layers=" + Arrays.toString(this.layers) + ']';
        }

    }
}