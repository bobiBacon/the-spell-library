package net.bobbacon.loot;

import net.bobbacon.TheSpellLibrary;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.SetInstrumentLootFunction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;

public class ModLoot {
    public static final LootFunctionType SET_SPELL= register("set_spell", new SetSpellFunction.Serializer());
    public static final LootFunctionType RANDOM_SPELL= register("random_spell", new RandomSpellLootFunction.Serializer());

    private static LootFunctionType register(String id, JsonSerializer<? extends LootFunction> jsonSerializer) {
        return Registry.register(Registries.LOOT_FUNCTION_TYPE, Identifier.of(TheSpellLibrary.MOD_ID,id), new LootFunctionType(jsonSerializer));
    }
    public static void init(){

    }
}
