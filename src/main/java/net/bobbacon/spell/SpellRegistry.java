package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class SpellRegistry {
    private static final RegistryKey<Registry<SpellType<?>>> SPELL_REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(TheSpellLibrary.MOD_ID, "spell"));
    public static final SimpleRegistry<SpellType<?>> SPELL_TYPES = FabricRegistryBuilder.createSimple(SPELL_REGISTRY_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    public static void init(){

    }
    public static SimpleRegistry<SpellType<?>> getSpellRegistry(){
        return SPELL_TYPES;
    }
}
