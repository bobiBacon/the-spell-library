package net.bobbacon.spell;

import com.google.common.collect.Maps;
import net.bobbacon.TheSpellLibrary;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.Map;

public class SpellCooldownManager {
    private final Map<SpellType<?>, SpellCooldownManager.Entry> entries = Maps.newHashMap();
    private int tick;

    public boolean isCoolingDown(SpellType<?> spellType) {
        boolean b= this.getCooldownProgress(spellType, 0.0F) > 0.0F;
        if (!b){
            remove(spellType);
        }
        return b;
    }

    public float getCooldownProgress(SpellType<?> spellType, float tickDelta) {
        SpellCooldownManager.Entry entry = this.entries.get(spellType);
        if (entry != null) {
            float f = entry.endTick - entry.startTick;
            float g = entry.endTick - (this.tick + tickDelta);
            return MathHelper.clamp(g / f, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void update() {
        if (!this.entries.isEmpty()) {
            this.tick++;
            Iterator<Map.Entry<SpellType<?>, SpellCooldownManager.Entry>> iterator = this.entries.entrySet().iterator();

            while (iterator.hasNext()) {
                java.util.Map.Entry<SpellType<?>, SpellCooldownManager.Entry> entry = iterator.next();
                TheSpellLibrary.LOGGER.info("cooling down");
                if (entry.getValue().endTick <= this.tick) {
                    iterator.remove();
                    this.onCooldownUpdate(entry.getKey());
                }
            }
        }
    }

    public void set(SpellType<?> spellType, int duration) {
        this.entries.put(spellType, new SpellCooldownManager.Entry(this.tick, this.tick + duration));
        this.onCooldownUpdate(spellType, duration);
    }

    public void remove(SpellType<?> spellType) {
        this.entries.remove(spellType);
        this.onCooldownUpdate(spellType);
    }

    protected void onCooldownUpdate(SpellType<?> spellType, int duration) {
    }

    protected void onCooldownUpdate(SpellType<?> spellType) {
    }

    static class Entry {
        final int startTick;
        final int endTick;

        Entry(int startTick, int endTick) {
            this.startTick = startTick;
            this.endTick = endTick;
        }
    }
}
