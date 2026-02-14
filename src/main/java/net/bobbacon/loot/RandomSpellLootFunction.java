package net.bobbacon.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellDefs;
import net.bobbacon.spell.SpellRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.village.VillagerProfession;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class RandomSpellLootFunction extends ConditionalLootFunction {
    protected final Predicate<LootContext> predicate;

    protected RandomSpellLootFunction(LootCondition[] conditions, Predicate<LootContext> predicate) {
        super(conditions);
        this.predicate = predicate;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
       if (!predicate.test(context)){
           return ItemStack.EMPTY;
       }

        List<SpellDef<?>> spells = SpellDefs.getAllDefaultLootTableSpells();

        if (spells.isEmpty()) return stack;

        SpellDef<?> spell = spells.get(
                context.getRandom().nextInt(spells.size())
        );

        ScrollItem.setSpell(stack, spell);

        return stack;
    }

    public static LootFunction.Builder builder(Predicate<LootContext> predicate) {
        return builder((Function<LootCondition[], LootFunction>) conditions -> new RandomSpellLootFunction(conditions, predicate));
    }

    @Override
    public LootFunctionType getType() {
        return ModLoot.RANDOM_SPELL;
    }
    public static class Serializer extends ConditionalLootFunction.Serializer<RandomSpellLootFunction> {
        public void toJson(JsonObject jsonObject, RandomSpellLootFunction randomSpellFunction, JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, randomSpellFunction, jsonSerializationContext);
            jsonObject.addProperty("predicate", Predicates.PREDICATES.getId(randomSpellFunction.predicate).toString());
        }

        public RandomSpellLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            String string = JsonHelper.getString(jsonObject, "predicate");
            Predicate<?> predicate = Predicates.PREDICATES
                    .getOrEmpty(Identifier.tryParse(string))
                    .orElseThrow(() -> new JsonSyntaxException("Unknown condition '" + string + "'"));

            return new RandomSpellLootFunction(lootConditions, (Predicate<LootContext>) predicate);
        }
    }
}
