package xyz.amymialee.mialeemisc.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.mialeemisc.MialeeMisc;
import xyz.amymialee.mialeemisc.events.AutoSmeltingCallback;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void mialeeMisc$autoSmelting(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld ) {
            var result = AutoSmeltingCallback.EVENT.invoker().interact(state, world, pos, blockEntity, entity, stack);
            if (result == ActionResult.SUCCESS) {
                Block.getDroppedStacks(state, serverWorld, pos, blockEntity, entity, stack).forEach((stackX) -> {
                    var smelted = simulateSmelt(world, stackX);
                    if (smelted != null) {
                        Block.dropStack(world, pos, smelted);
                        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                        serverWorld.spawnParticles(stack.isIn(MialeeMisc.NETHERITE_TOOLS) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 8, 0.25D, 0.25D, 0.25D, 0.025D);
                    } else {
                        Block.dropStack(world, pos, stackX);
                    }
                });
                state.onStacksDropped(serverWorld, pos, stack, true);
                ci.cancel();
            }
        }
    }

    @Unique
    private static final SimpleInventory fakeFurnace = new SimpleInventory(3);

    @Unique
    private static ItemStack simulateSmelt(World world, ItemStack input) {
        fakeFurnace.clear();
        fakeFurnace.setStack(0, input);
        var recipes = world.getRecipeManager().getAllMatches(RecipeType.SMELTING, fakeFurnace, world);
        for (var recipe : recipes) {
            ItemStack baseOutput = recipe.getOutput(world.getRegistryManager());
            if (baseOutput != null && !baseOutput.isEmpty()) {
                var output = baseOutput.copy();
                output.setCount(output.getCount() * input.getCount());
                return output;
            }
        }
        return null;
    }
}