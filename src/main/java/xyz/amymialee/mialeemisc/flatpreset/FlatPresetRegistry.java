package xyz.amymialee.mialeemisc.flatpreset;

import net.minecraft.item.ItemConvertible;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;

import java.util.ArrayList;
import java.util.Set;

public class FlatPresetRegistry {
    private static final ArrayList<FlatPresetEntry> entries = new ArrayList<>();

    public static void addEntry(FlatPresetEntry entry) {
        entries.add(entry);
        //saveEntries();
    }

    public static ArrayList<FlatPresetEntry> getEntries() {
//        Set<File> files = listPossiblePresets(MinecraftClient.getInstance().runDirectory + "config\\mialeemisc\\flatpresets");
//        for (File file : files) {
//
//        }
        return entries;
    }

//    private static Set<File> listPossiblePresets(String dir) {
//        File[] files = new File(dir).listFiles();
//        if (files == null) return Set.of();
//        return Stream.of(files).filter(file -> !file.isDirectory()).collect(Collectors.toSet());
//    }

    public record FlatPresetEntry(Identifier identifier, ItemConvertible icon, RegistryKey<Biome> biome, Set<RegistryKey<StructureSet>> structureSets, boolean hasFeatures, boolean hasLakes, FlatChunkGeneratorLayer... layers) {}
}