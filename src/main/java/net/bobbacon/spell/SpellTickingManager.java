package net.bobbacon.spell;

import java.util.ArrayList;

public class SpellTickingManager {
    protected final ArrayList<TickedSpell> spellsToTick= new ArrayList<>();
    public void addSpell(TickedSpell spell){
        spellsToTick.add(spell);
    }
    public void removeSpell(TickedSpell spell){
        spellsToTick.remove(spell);
    }
    public void tickAll(){

        spellsToTick.removeIf(tickedSpell ->{
            try{
                Spell spell= (Spell) tickedSpell;
                spell.age++;
                boolean remove= tickedSpell.tick();
                if (spell.age> tickedSpell.getMaxTime()||!spell.casted){
                    return true;
                }
                return remove;
            }catch (Exception e){
                return true;
            }
        });

    }
}
