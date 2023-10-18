package xyz.amymialee.mialeemisc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface AutoSmeltingCallback {

    interface AutoSmeltingCallbackInterface {
        ActionResult interact(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack);
    }
    Event<AutoSmeltingCallbackInterface> EVENT = EventFactory.createArrayBacked(AutoSmeltingCallbackInterface.class,
            (listeners) -> (state, world, pos, blockEntity, entity, stack) -> {
                for (var listener : listeners) {
                    var result = listener.interact(state, world, pos, blockEntity, entity, stack);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );
}

