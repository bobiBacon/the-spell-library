package net.bobbacon.spell;

public interface TickedSpell {
    public void tick();
    public int getMaxTime();
    public void abort();
}
