package net.bobbacon.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellRegistry;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class SetSpellFunction extends ConditionalLootFunction {
    final SpellDef<?> spellDef;

    public SetSpellFunction(LootCondition[] conditions, SpellDef<?> spellDef) {
        super(conditions);
        this.spellDef= spellDef;
    }

    @Override
    public LootFunctionType getType() {
        return ModLoot.SET_SPELL;
    }

    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        ScrollItem.setSpell(stack,spellDef);
        return stack;
    }

    public static ConditionalLootFunction.Builder<?> builder(SpellDef<?> spellDef) {
        return builder(conditions -> new SetSpellFunction(conditions, spellDef));
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<SetSpellFunction> {
        public void toJson(JsonObject jsonObject, SetSpellFunction setSpellFunction, JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, setSpellFunction, jsonSerializationContext);
            jsonObject.addProperty("id", SpellRegistry.SPELL_TYPES.getId(setSpellFunction.spellDef).toString());
        }

        public SetSpellFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            String string = JsonHelper.getString(jsonObject, "id");
            SpellDef<?> spellDef1 = SpellRegistry.SPELL_TYPES
                    .getOrEmpty(Identifier.tryParse(string))
                    .orElseThrow(() -> new JsonSyntaxException("Unknown spell '" + string + "'"));
            return new SetSpellFunction(lootConditions, spellDef1);
        }
    }
}

