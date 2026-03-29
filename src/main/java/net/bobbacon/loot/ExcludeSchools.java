package net.bobbacon.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellSchool;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcludeSchools implements SpellLootModifier{
    public List<SpellSchool> schools;

    public ExcludeSchools(List<SpellSchool> schools) {
        this.schools = schools;
    }

    public ExcludeSchools() {
    }

    @Override
    public void apply(Map<SpellDef<?>, Integer> map) {
        for (SpellDef<?> spellDef:map.keySet()){
            if (schools.contains(spellDef.school)){
                map.put(spellDef,0);
            }
        };
    }

    @Override
    public JsonElement toJson() {
        return null;
    }

    @Override
    public SpellLootModifier fromJson(JsonElement element) {
        List<SpellSchool> schools= new ArrayList<>();
        JsonArray schoolIds= JsonHelper.getArray(element.getAsJsonObject(),"schools",null);
        for (JsonElement element1: schoolIds){
            SpellSchool school= SpellRegistry.SPELL_SCHOOL.getOrEmpty(Identifier.tryParse(element1.getAsString()))
                    .orElseThrow(() -> new JsonSyntaxException("Unknown condition '" + element1.getAsString() + "'"));
            schools.add(school);
        }

        return new ExcludeSchools(this.schools);
    }
}
