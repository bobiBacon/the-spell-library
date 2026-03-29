package net.bobbacon.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellSchool;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Map;

public class BySchoolModifier implements SpellLootModifier{
    public  float modifier;
    public  SpellSchool school;

    public BySchoolModifier(float modifier, SpellSchool school) {
        this.modifier = modifier;
        this.school = school;
    }

    public BySchoolModifier() {
    }

    @Override
    public void apply(Map<SpellDef<?>,Integer> map){
        for (SpellDef<?> spellDef:map.keySet()){
            if (spellDef.school==school){
                map.put(spellDef,(int)(map.get(spellDef)*modifier));
            }
        }
    }

    @Override
    public JsonElement toJson() {
        JsonObject object= new JsonObject();
        object.addProperty("school", SpellRegistry.SPELL_SCHOOL.getId(this.school).toString());
        object.addProperty("modifier",this.modifier);

        SpellLootModifiers.addTypeToJson(object,SpellLootModifiers.ModifierOnSchools);

        return object;
    }

    @Override
    public SpellLootModifier fromJson(JsonElement element) {
        float modifier= JsonHelper.getFloat(element.getAsJsonObject(),"modifier",1f);


        String schoolId= JsonHelper.getString(element.getAsJsonObject(),"school",null);
        SpellSchool school= SpellRegistry.SPELL_SCHOOL.getOrEmpty(Identifier.tryParse(schoolId))
                .orElseThrow(() -> new JsonSyntaxException("Unknown condition '" + schoolId + "'"));
        return new BySchoolModifier(modifier,school);
    }


}
