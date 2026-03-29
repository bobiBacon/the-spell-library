package net.bobbacon.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class SpellLootModifiers {
    private static final RegistryKey<Registry<SpellLootModifier>> SpellLootModifier_Key =
            RegistryKey.ofRegistry(new Identifier(TheSpellLibrary.MOD_ID, "spell_loot_modifier"));
    public static final SimpleRegistry<SpellLootModifier> spellLootModifier = FabricRegistryBuilder.createSimple(SpellLootModifier_Key)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    private static final RegistryHelper<SpellLootModifier> registryHelper= new RegistryHelper<>(spellLootModifier,TheSpellLibrary.MOD_ID);
    public static final SpellLootModifier ExcludeSchools=registryHelper.register("exclude_schools",new ExcludeSchools());
    public static final SpellLootModifier OnlySchools=registryHelper.register("only_schools",new OnlySchools());
    public static final SpellLootModifier ModifierOnSchools=registryHelper.register("modifier_on_schools",new BySchoolModifier());
    public static void init(){

    }
    public static SpellLootModifier formJson(JsonElement element){
        String typeId= JsonHelper.getString(element.getAsJsonObject(),"type");
        SpellLootModifier template= spellLootModifier.getOrEmpty(Identifier.tryParse(typeId))
                .orElseThrow(() -> new JsonSyntaxException("Unknown condition '" + typeId + "'"));
        return template.fromJson(element);
    }
    public static void addTypeToJson(JsonObject object,SpellLootModifier type){
        object.addProperty("type", String.valueOf(spellLootModifier.getId(type)));
    }
}
