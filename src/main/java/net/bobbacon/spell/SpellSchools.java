package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class SpellSchools {
    private static  final RegistryHelper<SpellSchool> registryHelper= new RegistryHelper<>(SpellRegistry.SPELL_SCHOOL, TheSpellLibrary.MOD_ID);
    public static final SpellSchool FIRE= registryHelper.register("fire", new SpellSchool(Style.EMPTY.withColor(Formatting.DARK_RED)));
    public static final SpellSchool Necromancy= registryHelper.register("necromancy", new SpellSchool(Style.EMPTY.withColor(Formatting.DARK_PURPLE).withBold(true)));
    public static final SpellSchool Conjuration= registryHelper.register("conjuration", new SpellSchool(Style.EMPTY.withColor(Formatting.BLUE).withBold(true)));
    public static final SpellSchool Arcanic= registryHelper.register("arcanic", new SpellSchool(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)));

    public static void init(){}
}
