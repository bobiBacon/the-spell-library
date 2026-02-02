package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;

public class SpellTypes {
    private static final RegistryHelper<SpellType<?>> registryHelper= new RegistryHelper<>(SpellRegistry.SPELL_TYPES, TheSpellLibrary.MOD_ID);

    public static final SpellType<?> Example= registryHelper.register("example",new SpellType<>(ExampleSpell::new,20).cooldown(60));
    public static final SpellType<?> FireBall= registryHelper.register("fire_ball",new SpellType<>(ProjectileShootingSpell::new,20).cooldown(60));
    public static final SpellType<?> EMPTY= registryHelper.register("empty",new SpellType<>(Spell::new,0));
    public static void init(){

    }

}
