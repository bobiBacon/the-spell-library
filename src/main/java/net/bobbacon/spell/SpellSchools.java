package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class SpellSchools {
    private static  final RegistryHelper<SpellSchool> registryHelper= new RegistryHelper<>(SpellRegistry.SPELL_SCHOOL, TheSpellLibrary.MOD_ID);
    public static final SpellSchool Fire = registryHelper.register("fire", new SpellSchool(Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true),0xBD0F0F));
    public static final SpellSchool Necromancy= registryHelper.register("necromancy", new SpellSchool(Style.EMPTY.withColor(Formatting.DARK_PURPLE).withBold(true),0x420059));
    public static final SpellSchool Conjuration= registryHelper.register("conjuration", new SpellSchool(Style.EMPTY.withColor(Formatting.DARK_AQUA).withBold(true),0x6CD6E0));
    public static final SpellSchool Arcanic= registryHelper.register("arcanic", new SpellSchool(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true),0xBD13BA));
    public static final SpellSchool Wind= registryHelper.register("wind", new SpellSchool(Style.EMPTY.withColor(Formatting.GRAY).withBold(true),0xBEC8D4));
    public static final SpellSchool Divine= registryHelper.register("divine", new SpellSchool(Style.EMPTY.withColor(Formatting.GOLD).withBold(true),0xFFB900));
    public static final SpellSchool Thunder= registryHelper.register("thunder", new SpellSchool(Style.EMPTY.withColor(Formatting.BLUE).withBold(true),0x0058CC));

    public static void init(){}
}
