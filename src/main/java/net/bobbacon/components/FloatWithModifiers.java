package net.bobbacon.components;

import net.bobbacon.nbt.NbtSerializable;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FloatWithModifiers implements NbtSerializable {
    final float baseValue;
    HashMap<UUID,Modifier> modifiers= new HashMap<>();

    public FloatWithModifiers(float baseValue) {
        this.baseValue = baseValue;
    }


    public float getValue() {
        float additive=0;
        float factor=1;
        for (Modifier modifier: modifiers.values()){
            switch (modifier.operationType){
                case ADDITION -> {
                    additive+= modifier.modifier;
                }
                case MULTIPLICATION -> {
                    factor*= modifier.modifier;
                }
            }
        }
        return factor*(baseValue+additive);
    }
    public void clear(){
        modifiers.clear();
    }

    public void addModifier(UUID uuid,Modifier modifier){
        modifiers.put(uuid,modifier);
    }
    public void addModifier(UUID uuid,float value, OperationType operationType){
        addModifier(uuid,new Modifier(value,operationType));
    }
    public void removeModifier(UUID uuid){
        modifiers.remove(uuid);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        modifiers.clear();
        NbtCompound modifiersTag= tag.getCompound("modifiers");
        for (var key:modifiersTag.getKeys()){
            UUID id= UUID.fromString(key);
            NbtCompound modifierTag= modifiersTag.getCompound(key);

            modifiers.put(id,new Modifier(modifierTag));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound modifiersTag= new NbtCompound();
        for (var entry: modifiers.entrySet()){
            NbtCompound modifierTag= new NbtCompound();
            entry.getValue().writeToNbt(modifierTag);
            modifiersTag.put(entry.getKey().toString(),modifierTag);
        }
        tag.put("modifiers",modifiersTag);
    }

    public class Modifier implements NbtSerializable{
        float modifier;
        OperationType operationType;

        public Modifier(float modifier, OperationType operationType) {
            this.modifier = modifier;
            this.operationType = operationType;
        }
        public Modifier(NbtCompound tag) {
            this();
            readFromNbt(tag);
        }

        private Modifier() {
        }

        @Override
        public void readFromNbt(NbtCompound tag) {
            modifier= tag.getFloat("value");
            operationType= OperationType.valueOf(tag.getString("operation"));
        }

        @Override
        public void writeToNbt(NbtCompound tag) {
            tag.putFloat("value",modifier);
            tag.putString("operation", operationType.name());
        }
    }
    public enum OperationType{
        ADDITION,
        MULTIPLICATION
    }
}
