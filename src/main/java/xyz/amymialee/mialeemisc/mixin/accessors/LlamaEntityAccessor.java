package xyz.amymialee.mialeemisc.mixin.accessors;

import net.minecraft.entity.passive.LlamaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LlamaEntity.class)
public interface LlamaEntityAccessor {
    @Accessor("spit")
    void mialeeMisc$setSpit(boolean spit);
}