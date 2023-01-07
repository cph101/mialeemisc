package xyz.amymialee.mialeemisc.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MialeeResourceLoader extends JsonDataLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//    public static ArrayList<Item> inventoryItems = new ArrayList<>();

    public MialeeResourceLoader() {
        super(GSON, "mialee_misc");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> map, ResourceManager manager, Profiler profiler) {
//        inventoryItems.clear();
//        for (Map.Entry<Identifier, JsonElement> entry : map.entrySet()) {
//            try {
//                Item[] array = this.getItemArray(entry.getValue());
//                if (array != null) {
//                    inventoryItems.addAll(Arrays.asList(array));
//                }
//            } catch (Exception ignored) {}
//        }
    }

    private Item[] getItemArray(JsonElement json) {
        if (!json.isJsonArray()) {
            return null;
        }
        List<Item> itemList = new ArrayList<>();
        json.getAsJsonArray().forEach(element -> {
            if (element.isJsonPrimitive()) {
                Identifier identifier = new Identifier(element.getAsString());
                Item item = Registry.ITEM.get(identifier);
                if (item.equals(Items.AIR)) {
                    return;
                }
                if (!itemList.contains(item)) {
                    itemList.add(item);
                }
            }
        });
        if (!itemList.isEmpty()) {
            return itemList.toArray(new Item[0]);
        }
        return null;
    }
}