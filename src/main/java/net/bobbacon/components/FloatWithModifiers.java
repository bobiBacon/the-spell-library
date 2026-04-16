package net.bobbacon.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FloatWithModifiers {
    float baseValue;
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

    public void setBaseValue(float baseValue) {
        this.baseValue = baseValue;
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

    public class Modifier{
        final float modifier;
        final OperationType operationType;

        public Modifier(float modifier, OperationType operationType) {
            this.modifier = modifier;
            this.operationType = operationType;
        }
    }
    public enum OperationType{
        ADDITION,
        MULTIPLICATION
    }
}
