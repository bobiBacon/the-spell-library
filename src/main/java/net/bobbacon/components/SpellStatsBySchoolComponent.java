package net.bobbacon.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellSchool;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.UUID;

public abstract class SpellStatsBySchoolComponent implements AutoSyncedComponent {
    private HashMap<SpellSchool, FloatWithModifiers> schoolsMap;
    private FloatWithModifiers classicManaValue;
    private final PlayerEntity player;
    public final float baseValue;

    public SpellStatsBySchoolComponent(PlayerEntity player, float baseValue) {
        schoolsMap = emptyMap(baseValue);
        classicManaValue=new FloatWithModifiers(baseValue);
        this.player=player;
        this.baseValue= baseValue;
    }

    private final HashMap<SpellSchool, FloatWithModifiers> emptyMap(float baseValue){
        HashMap<SpellSchool,FloatWithModifiers> map=new HashMap<>();

        SpellRegistry.SPELL_SCHOOL.forEach(school -> {
            map.put(school,new FloatWithModifiers(baseValue));
        });
        return map;
    }
    public void addModifier(SpellSchool school, UUID modifierId, float value, FloatWithModifiers.OperationType operationType){
        addModifier(getFloatWithModifiers(school),modifierId,value,operationType);
    }
    public void addModifier(UUID modifierId, float value, FloatWithModifiers.OperationType operationType){
        addModifier(classicManaValue,modifierId, value, operationType);
    }
    private void addModifier(FloatWithModifiers floatWithModifiers,UUID modifierId, float value, FloatWithModifiers.OperationType operationType) {
        floatWithModifiers.addModifier(modifierId, value, operationType);
        sync();
    }

    public void removeModifier(SpellSchool school, UUID modifierId){
        removeModifier(getFloatWithModifiers(school),modifierId);
    }

    public void removeModifier(UUID modifierId){
        removeModifier(classicManaValue,modifierId);
    }

    private void removeModifier(FloatWithModifiers floatWithModifiers, UUID modifierId) {
        floatWithModifiers.removeModifier(modifierId);
        sync();
    }

    public FloatWithModifiers getFloatWithModifiers(SpellSchool school){
        return schoolsMap.get(school);
    }
    public float getValue(SpellSchool school){
        return getFloatWithModifiers(school).getValue();
    }
    public float getClassicValue(){
        return classicManaValue.getValue();
    }
    public void sync(){
        getKey().sync(player);
    }
    public abstract ComponentKey<? extends SpellStatsBySchoolComponent> getKey();


    @Override
    public void readFromNbt(NbtCompound tag) {
        schoolsMap= emptyMap(baseValue);
        NbtCompound mapTag = tag.getCompound("schools_map");

        for (String key : mapTag.getKeys()) {
            SpellSchool school = SpellRegistry.SPELL_SCHOOL.get(Identifier.tryParse(key));

            schoolsMap.get(school).readFromNbt(mapTag.getCompound(key));

        }
        NbtCompound classicTag= tag.getCompound("classic_mana");
        classicManaValue.readFromNbt(classicTag);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound schoolsTag = new NbtCompound();

        for (var entry : schoolsMap.entrySet()) {
            NbtCompound modifiersTag = new NbtCompound();
            FloatWithModifiers value = entry.getValue();
            value.writeToNbt(modifiersTag);


            schoolsTag.put(String.valueOf(SpellRegistry.SPELL_SCHOOL.getId(entry.getKey())),modifiersTag);

        }

        tag.put("schools_map", schoolsTag);

        NbtCompound classicTag= new NbtCompound();
        classicManaValue.writeToNbt(classicTag);
        tag.put("classic_mana",classicTag);
    }
}
