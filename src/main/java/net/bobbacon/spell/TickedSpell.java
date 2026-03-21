package net.bobbacon.spell;

public interface TickedSpell {
    public boolean tick();
    public int getMaxTime();
}
