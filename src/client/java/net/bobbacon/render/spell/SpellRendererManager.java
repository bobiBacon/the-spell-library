package net.bobbacon.render.spell;

import net.bobbacon.spell.Spell;

import java.util.HashMap;

public class SpellRendererManager {
    protected final HashMap<Spell,SpellRenderer> spellRenderers=new HashMap<>();
    public void add(Spell spell,SpellRenderer spellRenderer){
        spellRenderers.put(spell,spellRenderer);
    }
    public SpellRenderer get(Spell spell){
        return spellRenderers.get(spell);
    }
    public SpellRenderer getOrCreate(Spell spell){
        SpellRenderer renderer = spellRenderers.get(spell);
        if(renderer==null){
            renderer=SpellRenderers.getRendererFactory(spell.type).create();
            add(spell,renderer);
        }
        return renderer;
    }

    public void remove(Spell spell) {
        spellRenderers.remove(spell);
    }
}
