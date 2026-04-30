package net.bobbacon.spell;

import net.bobbacon.Accessors.PlayerAccessor;
import net.bobbacon.components.FloatWithModifiers;
import net.bobbacon.components.ModComponents;
import net.bobbacon.nbt.NbtSerializable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.UUID;

public class Mana implements NbtSerializable {
    public float amount=0;
    public float overAmount=0;
    public FloatWithModifiers maxAmount=new FloatWithModifiers(0);
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

        return maxAmount.getValue();
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

    public void addMaxModifier(UUID id, float modifier, FloatWithModifiers.OperationType operationType){
        boolean b = amount >= getMax();
        maxAmount.addModifier(id,modifier,operationType);
        if (b){
            amount=getMax();
        }
        sync();
    }

    public void removeMaxModifier(UUID id){
        maxAmount.removeModifier(id);
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

    @Override
    public void readFromNbt(NbtCompound tag) {
        amount=tag.getFloat("amount");
        overAmount=tag.getFloat("over_amount");
        maxAmount.readFromNbt(tag.getCompound("max_amount"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("amount",amount);
        tag.putFloat("over_amount",overAmount);
        NbtCompound nbtCompound= new NbtCompound();
        maxAmount.writeToNbt(nbtCompound);
        tag.put("max_amount",nbtCompound);
    }
}
