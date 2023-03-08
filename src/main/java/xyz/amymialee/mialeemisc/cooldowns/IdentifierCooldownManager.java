package xyz.amymialee.mialeemisc.cooldowns;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.Map;

public class IdentifierCooldownManager {
    private final Map<Identifier, Entry> entries = Maps.newHashMap();
    private int tick;

    public static IdentifierCooldownManager get(PlayerEntity player) {
        if (player instanceof IdentifierCooldownHolder holder) {
            return holder.getIdentifierCooldownManager();
        }
        throw new IllegalArgumentException("PlayerEntity is not an instance of IdentifierCooldownHolder");
    }

    public boolean isCoolingDown(Identifier identifier) {
        return this.getCooldownProgress(identifier, 0.0F) > 0.0F;
    }

    public float getCooldownProgress(Identifier identifier, float tickDelta) {
        Entry entry = this.entries.get(identifier);
        if (entry != null) {
            float f = (float)(entry.endTick - entry.startTick);
            float g = (float)entry.endTick - ((float)this.tick + tickDelta);
            return MathHelper.clamp(g / f, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void update() {
        ++this.tick;
        if (!this.entries.isEmpty()) {
            Iterator<Map.Entry<Identifier, Entry>> iterator = this.entries.entrySet().iterator();
            while(iterator.hasNext()) {
                java.util.Map.Entry<Identifier, Entry> entry = iterator.next();
                if (entry.getValue().endTick <= this.tick) {
                    iterator.remove();
                    this.onCooldownUpdate(entry.getKey());
                }
            }
        }
    }

    public void set(Identifier identifier, int duration) {
        if (duration <= 0) {
            this.remove(identifier);
            return;
        }
        this.entries.put(identifier, new Entry(this.tick, this.tick + duration));
        this.onCooldownUpdate(identifier, duration);
    }

    public void remove(Identifier identifier) {
        this.entries.remove(identifier);
        this.onCooldownUpdate(identifier);
    }

    protected void onCooldownUpdate(Identifier identifier, int duration) {}

    protected void onCooldownUpdate(Identifier identifier) {}

    record Entry(int startTick, int endTick) {}
}