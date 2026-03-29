package net.bobbacon.loot;

import com.google.gson.JsonElement;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellSchool;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface SpellLootModifier {



       public void apply(Map<SpellDef<?>,Integer> map);
       public JsonElement toJson();
     public  SpellLootModifier fromJson(JsonElement element);
}
