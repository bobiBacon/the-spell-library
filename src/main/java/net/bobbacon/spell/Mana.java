package net.bobbacon.spell;

import net.bobbacon.Accessors.PlayerAccessor;
import net.bobbacon.components.ModComponents;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public class Mana {
    public float amount=0;
    public float overAmount=0;
    public final HashMap<UUID,Float> maxModifiers=new HashMap<>();
    public final PlayerEntity player;

    public Mana(PlayerEntity player) {
        this.player = player;
    }

    public float getTotalMana() {
        return amount+overAmount;
    }
    public float getManaPoints() {
        return amount;
    }

    public float getOverAmount() {
        return overAmount;
    }

    public float getMax() {
        float max = 0;

        for (float f : maxModifiers.values()) {
            max +=f;
        }
        return max;
    }

    public void useMana(float amount) {
        if (amount>=overAmount&&overAmount>0){
            amount -= overAmount;
            overAmount=0;
        } else if (amount < overAmount) {
            overAmount-=amount;
            amount=0;
        }
        this.amount -= amount;
        if (this.amount<0){
            this.amount=0;
        }
        sync();
    }
    public void addMana(float amount) {
        this.amount += amount;
        if (amount>=getMax()){
            this.amount=getMax();
        }
        sync();
    }

    public void addOverMana(float overAmount) {
        this.overAmount += overAmount;
        sync();
    }

    public void addMaxModifier(UUID id,float modifier){
        boolean b = amount >= getMax();
        maxModifiers.put(id,modifier);
        if (b){
            amount=getMax();
        }
        sync();
    }

    public void removeMaxModifier(UUID id){
        maxModifiers.remove(id);
        if (amount>=getMax()){
            amount=getMax();
        }
        sync();
    }
    public void sync(){
        ModComponents.SCHOOLS_MANA.sync(player);
    }

    public static HashMap<SpellSchool,Mana> getEmptyMap(PlayerEntity player){
        HashMap<SpellSchool,Mana> map=new HashMap<>();
        SpellRegistry.SPELL_SCHOOL.forEach(school -> {
            map.put(school,new Mana(player));
        });
        return map;
    }
    public static HashMap<SpellSchool,Mana> getSchoolsMana(PlayerEntity player){
        return ModComponents.SCHOOLS_MANA.get(player).getSchoolsMana();
    }
    public static float getMaxMana(PlayerEntity player){
        float max=0;
        PlayerAccessor playerAccessor= (PlayerAccessor) player;
        max+= playerAccessor.the_spell_library$getMaxMana();
        for (var mana:getSchoolsMana(player).values()){
            max+=mana.getMax();
        }
        return max;
    }
    public static float getClassicMana(PlayerEntity player){
        PlayerAccessor playerAccessor= (PlayerAccessor) player;
        return playerAccessor.the_spell_library$getMana();
    }
    public static float getMaxClassicMana(PlayerEntity player){
        PlayerAccessor playerAccessor= (PlayerAccessor) player;
        return playerAccessor.the_spell_library$getMaxMana();
    }
}
