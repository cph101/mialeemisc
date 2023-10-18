package xyz.amymialee.mialeemisc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;

public interface ImperceptibleCallback {
    Event<ImperceptibleCallback> EVENT = EventFactory.createArrayBacked(ImperceptibleCallback.class,
            (listeners) -> (entity) -> {
                for (var listener : listeners) {
                    var result = listener.interact(entity);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(Entity entity);
}