package xyz.amymialee.mialeemisc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.mialeemisc.itemgroup.MialeeItemGroup;
import xyz.amymialee.mialeemisc.util.MialeeMath;

public class MialeeMisc implements ModInitializer {
    public static final String MOD_ID = "mialeemisc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.info("Loaded in development environment.");
            MialeeItemGroup.create(id(MOD_ID, "mialee_group"))
                    .setIcon((i) -> {
                        Item item = Registry.ITEM.get(MialeeMath.clampLoop(i / 2 + 1, 1, Registry.ITEM.size()));
                        return new ItemStack(item);
                    })
                    .setItems((itemStacks, itemGroup) -> {
                        for(Item item : Registry.ITEM) {
                            if (item == Items.AIR) continue;
                            itemStacks.add(item.getDefaultStack());
                        }
                    });
        }
    }

    public static Identifier id(String namespace, String... path) {
        return new Identifier(namespace, String.join(".", path));
    }
}