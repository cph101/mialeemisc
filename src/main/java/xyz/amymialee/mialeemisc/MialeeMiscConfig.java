package xyz.amymialee.mialeemisc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.PresetsScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MialeeMiscConfig {
    private static final Path configFile = FabricLoader.getInstance().getConfigDir().resolve(MialeeMisc.MOD_ID + ".json");
    public static Map<Identifier, FlatChunkGeneratorConfig> flatPresets = new HashMap<>();

    protected static void saveConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = new JsonObject();
            JsonArray flatJson = new JsonArray();
            if (flatPresets != null) {
                for (Map.Entry<Identifier, FlatChunkGeneratorConfig> preset : flatPresets.entrySet()) {
                    JsonObject presetJson = new JsonObject();
                    presetJson.addProperty("name", preset.getKey().toString());
                    presetJson.addProperty("config", PresetsScreen.getGeneratorConfigString(preset.getValue()));
                }
            }
            json.add("flatPresets", flatJson);
            String jsonData = gson.toJson(json);
            Files.writeString(configFile, jsonData);
        } catch (Exception e) {
            MialeeMisc.LOGGER.info(e.toString());
        }
    }

    protected static void loadConfig() {
        try {
            Gson gson = new Gson();
            String reader = Files.readString(configFile);
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            if (data.has("flatPresets")) {
                JsonArray flatJson = data.getAsJsonArray("flatPresets");
                ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
                if (networkHandler != null) {
                    DynamicRegistryManager registryManager = networkHandler.getRegistryManager();
                    Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
                    Registry<StructureSet> structureSetRegistry = registryManager.get(Registry.STRUCTURE_SET_KEY);
                    flatPresets = new HashMap<>();
                    for (int i = 0; i < flatJson.size(); i++) {
                        JsonObject preset = flatJson.get(i).getAsJsonObject();
                        Identifier name = new Identifier(preset.get("name").getAsString());
                        FlatChunkGeneratorConfig config = PresetsScreen.parsePresetString(biomeRegistry, structureSetRegistry, preset.get("config").getAsString(), FlatChunkGeneratorConfig.getDefaultConfig(biomeRegistry, structureSetRegistry));
                        flatPresets.put(name, config);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            MialeeMisc.LOGGER.info("Config data not found.");
        } catch (Exception e) {
            MialeeMisc.LOGGER.info("Error loading config data.");
            MialeeMisc.LOGGER.info(e.toString());
        }
    }
}