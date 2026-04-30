package net.bobbacon.Accessors;

import net.bobbacon.spell.Mana;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellSchool;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;

public interface PlayerAccessor {
    public float the_spell_library$getMana();
    public float the_spell_library$getMaxMana();
    public float the_spell_library$getManaRegenRate();
    public float the_spell_library$getManaRegenRate(SpellSchool school);
    public void the_spell_library$setMaxMana(float amount);
    public void the_spell_library$setMana(float amount);
    public void the_spell_library$incrementMana(float amount);
    public void the_spell_library$incrementMana();
    public void the_spell_library$decrementMana(float amount);
    public void the_spell_library$decrementMana();
    @Unique
    public Spell getCurrentlyCastingSpell();

    @Unique
    public void setCurrentlyCastingSpell(Spell currentlyCastingSpell);
}
