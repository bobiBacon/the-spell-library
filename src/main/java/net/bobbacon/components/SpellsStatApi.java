package net.bobbacon.components;

import net.bobbacon.spell.SpellSchool;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpellsStatApi {
    public static float getStat(SpellStatsBySchoolComponent component, SpellSchool school){
        return component.getValue(school);
    }
    public static float getStat(SpellStatsBySchoolComponent component){
        return component.getClassicValue();
    }
    public static void addModifier(SpellStatsBySchoolComponent component, SpellSchool school,UUID modifierId, float value, FloatWithModifiers.OperationType operationType){
        component.addModifier(school,modifierId,value,operationType);
    }
    public static void removeModifier(SpellStatsBySchoolComponent component, SpellSchool school,UUID modifierId){
        component.removeModifier(school,modifierId);
    }
    public static void addModifier(SpellStatsBySchoolComponent component, UUID modifierId, float value, FloatWithModifiers.OperationType operationType){
        component.addModifier(modifierId,value,operationType);
    }
    public static void removeModifier(SpellStatsBySchoolComponent component, UUID modifierId){
        component.removeModifier(modifierId);
    }



    public static ManaRegenComponent getManaRegenComponent(PlayerEntity player) {
        return ModComponents.MANA_REGEN.get(player);
    }
    public static SpellPowerComponent getSpellPowerComponent(PlayerEntity player) {
        return ModComponents.SPELL_POWER.get(player);
    }
    public static CooldownSpeedComponent getCooldownSpeedComponent(PlayerEntity player) {
        return ModComponents.COOLDOWN_SPEED.get(player);
    }
    public static CastSpeedComponent getCastSpeedComponent(PlayerEntity player) {
        return ModComponents.CAST_SPEED.get(player);
    }



}
