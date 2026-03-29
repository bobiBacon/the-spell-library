package net.bobbacon.loot;

import com.google.gson.*;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.*;
import net.bobbacon.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Rarity;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class RandomSpellLootFunction extends ConditionalLootFunction {
    protected final Predicate<LootContext> predicate;
    protected final List<SpellLootModifier> modifiers;

    protected RandomSpellLootFunction(LootCondition[] conditions, Predicate<LootContext> predicate, List<SpellLootModifier> modifiers) {
        super(conditions);
        if (predicate==null){
            predicate=Predicates.AlwaysTrueLoot;
        }
        this.predicate = predicate;
        this.modifiers= modifiers;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
       if (!predicate.test(context)){
           return ItemStack.EMPTY;
       }

        List<SpellDef<?>> spells = SpellDefs.getAllDefaultLootTableSpells();

        if (spells.isEmpty()) return stack;
        spells.sort(Comparator.comparing(e -> e.rarity));
        Map<Integer, Integer> RarityPool = new HashMap<>();
        RarityPool.put(0,85);
        RarityPool.put(1,10);
        RarityPool.put(2,4);
        RarityPool.put(3,1);

        int raritySelector= Utils.weightedRandom(RarityPool);
//        int index= (int) Math.floor(2.2*Math.pow(raritySelector+0.1f,6));
        List<SpellDef<?>> list= SpellDef.getSpellsByRarity(Rarity.values()[raritySelector],spells);
        Map<SpellDef<?>,Integer> pool = new HashMap<>();
        for (SpellDef<?> def:list){
            pool.put(def,10000);
        }
        for (SpellLootModifier modifier:modifiers){
            modifier.apply(pool);
        }


        SpellDef<?> spell = Utils.weightedRandom(pool);
        ScrollItem.setSpell(stack, spell);

        return stack;
    }

    public static LootFunction.Builder builder(Predicate<LootContext> predicate,List<SpellLootModifier> modifiers) {
        return builder((Function<LootCondition[], LootFunction>) conditions -> new RandomSpellLootFunction(conditions, predicate,modifiers));
    }

    @Override
    public LootFunctionType getType() {
        return ModLoot.RANDOM_SPELL;
    }
    public static class Serializer extends ConditionalLootFunction.Serializer<RandomSpellLootFunction> {
        public void toJson(JsonObject jsonObject, RandomSpellLootFunction randomSpellFunction, JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, randomSpellFunction, jsonSerializationContext);
            jsonObject.addProperty("predicate", Predicates.PREDICATES.getId(randomSpellFunction.predicate).toString());
            JsonArray array= new JsonArray();
            for (SpellLootModifier modifier: randomSpellFunction.modifiers){

                array.add(modifier.toJson());
            }
            jsonObject.add("modifiers",array);
        }

        public RandomSpellLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            String string = JsonHelper.getString(jsonObject, "predicate",null);
            Predicate<?> predicate = string==null? Predicates.AlwaysTrueLoot:Predicates.PREDICATES
                    .getOrEmpty(Identifier.tryParse(string))
                    .orElseThrow(() -> new JsonSyntaxException("Unknown condition '" + string + "'"));
            JsonArray array=JsonHelper.getArray(jsonObject,"modifiers");
            ArrayList<SpellLootModifier> modifiers= new ArrayList<>();
            for (JsonElement object:array){
                modifiers.add(SpellLootModifiers.formJson(object));
            }
            return new RandomSpellLootFunction(lootConditions, (Predicate<LootContext>) predicate,modifiers);
        }
    }
}
