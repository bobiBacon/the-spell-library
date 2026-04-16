package net.bobbacon.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.bobbacon.spell.Mana;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellSchool;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.UUID;

public abstract class SpellStatsBySchoolComponent implements AutoSyncedComponent {
    private final HashMap<SpellSchool, FloatWithModifiers> schoolsMap;
    private final PlayerEntity player;

    public SpellStatsBySchoolComponent(PlayerEntity player, float baseValue) {
        schoolsMap = emptyMap(baseValue);
        this.player=player;
    }

    private final HashMap<SpellSchool, FloatWithModifiers> emptyMap(float baseValue){
        HashMap<SpellSchool,FloatWithModifiers> map=new HashMap<>();

        SpellRegistry.SPELL_SCHOOL.forEach(school -> {
            map.put(school,new FloatWithModifiers(baseValue));
        });
        return map;
    }
    public void addModifier(SpellSchool school, UUID modifierId, float value, FloatWithModifiers.OperationType operationType){
        getFloatWithModifiers(school).addModifier(modifierId,value,operationType);
        sync();
    }
    public void removeModifier(SpellSchool school, UUID modifierId){
        getFloatWithModifiers(school).removeModifier(modifierId);
        sync();
    }
    public FloatWithModifiers getFloatWithModifiers(SpellSchool school){
        return schoolsMap.get(school);
    }
    public float getValue(SpellSchool school){
        return getFloatWithModifiers(school).getValue();
    }
    public void sync(){
        getKey().sync(player);
    }
    public abstract ComponentKey<? extends SpellStatsBySchoolComponent> getKey();


    @Override
    public void readFromNbt(NbtCompound tag) {

        NbtCompound mapTag = tag.getCompound("schools_map");

        for (String key : mapTag.getKeys()) {
            SpellSchool school = SpellRegistry.SPELL_SCHOOL.get(Identifier.tryParse(key));

            schoolsMap.get(school).clear();

            NbtCompound modifiersTag = mapTag.getCompound(key);
            for (String key2 : modifiersTag.getKeys()) {
                NbtCompound modifierTag = modifiersTag.getCompound(key2);

                schoolsMap.get(school).addModifier(UUID.fromString(key2),modifierTag.getFloat("value"), FloatWithModifiers.OperationType.valueOf(modifierTag.getString("operation")));

            }

        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound schoolsTag = new NbtCompound();

        for (var entry : schoolsMap.entrySet()) {
            NbtCompound modifiersTag = new NbtCompound();
            FloatWithModifiers value = entry.getValue();

            for (var entry2: value.modifiers.entrySet()){
                NbtCompound modifierTag = new NbtCompound();
                modifierTag.putFloat("value",entry2.getValue().modifier);
                modifierTag.putString("operation",entry2.getValue().operationType.name());
                modifiersTag.put(entry2.getKey().toString(),modifierTag);
            }
            schoolsTag.put(String.valueOf(SpellRegistry.SPELL_SCHOOL.getId(entry.getKey())),modifiersTag);

        }

        tag.put("schools_map", schoolsTag);
    }
}
