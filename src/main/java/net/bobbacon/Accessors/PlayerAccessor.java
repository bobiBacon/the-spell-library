package net.bobbacon.Accessors;

public interface PlayerAccessor {
    public float the_spell_library$getMana();
    public float the_spell_library$getMaxMana();
    public float the_spell_library$getManaRegenRate();
    public void the_spell_library$setMaxMana(float amount);
    public void the_spell_library$setMana(float amount);
    public void the_spell_library$incrementMana(float amount);
    public void the_spell_library$incrementMana();
    public void the_spell_library$decrementMana(float amount);
    public void the_spell_library$decrementMana();
}
