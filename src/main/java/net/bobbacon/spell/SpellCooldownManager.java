package net.bobbacon.spell;

import com.google.common.collect.Maps;
import net.bobbacon.TheSpellLibrary;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.Map;

public class SpellCooldownManager {
    private final Map<SpellDef<?>, SpellCooldownManager.Entry> entries = Maps.newHashMap();
    private int tick;

    public boolean isCoolingDown(SpellDef<?> spellDef) {
        boolean b= this.getCooldownProgress(spellDef, 0.0F) > 0.0F;
        if (!b){
            remove(spellDef);
        }
        return b;
    }

    public float getCooldownProgress(SpellDef<?> spellDef, float tickDelta) {
        SpellCooldownManager.Entry entry = this.entries.get(spellDef);
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
            Iterator<Map.Entry<SpellDef<?>, SpellCooldownManager.Entry>> iterator = this.entries.entrySet().iterator();

            while (iterator.hasNext()) {
                java.util.Map.Entry<SpellDef<?>, SpellCooldownManager.Entry> entry = iterator.next();
                if (entry.getValue().endTick <= this.tick) {
                    iterator.remove();
                    this.onCooldownUpdate(entry.getKey());
                }
            }
        }
    }

    public void set(SpellDef<?> spellDef, int duration) {
        this.entries.put(spellDef, new SpellCooldownManager.Entry(this.tick, this.tick + duration));
        this.onCooldownUpdate(spellDef, duration);
    }

    public void remove(SpellDef<?> spellDef) {
        this.entries.remove(spellDef);
        this.onCooldownUpdate(spellDef);
    }

    protected void onCooldownUpdate(SpellDef<?> spellDef, int duration) {
    }

    protected void onCooldownUpdate(SpellDef<?> spellDef) {
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
