package xyz.amymialee.mialeemisc.util;

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
    Event<AutoSmeltingCallback> EVENT = EventFactory.createArrayBacked(AutoSmeltingCallback.class,
            (listeners) -> (state, world, pos, blockEntity, entity, stack) -> {
                for (AutoSmeltingCallback listener : listeners) {
                    ActionResult result = listener.interact(state, world, pos, blockEntity, entity, stack);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack);
}