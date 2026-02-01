package net.bobbacon.attributes;

import net.bobbacon.TheSpellLibrary;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntityAttributes {
    public static final EntityAttribute GENERIC_MANA_REGEN = register(
            "generic.mana_regen", new ClampedEntityAttribute("attribute.name.generic.mana_regen", 20.0, 0, 20.0).setTracked(true)
    );
    private static EntityAttribute register(String id, EntityAttribute attribute) {
        return Registry.register(Registries.ATTRIBUTE, Identifier.of(TheSpellLibrary.MOD_ID,id), attribute);
    }
    public static void init(){

    }
}
