package net.bobbacon.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.bobbacon.TheSpellLibrary;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<ManaComponent> SCHOOLS_MANA =
            ComponentRegistry.getOrCreate(
                    new Identifier(TheSpellLibrary.MOD_ID, "schools_mana"),
                    ManaComponent.class
            );
    public static final ComponentKey<ManaRegenComponent> MANA_REGEN =
            ComponentRegistry.getOrCreate(
                    new Identifier(TheSpellLibrary.MOD_ID, "mana_regen"),
                    ManaRegenComponent.class
            );
    public static final ComponentKey<CooldownSpeedComponent> COOLDOWN_SPEED =
            ComponentRegistry.getOrCreate(
                    new Identifier(TheSpellLibrary.MOD_ID, "cooldown_speed"),
                    CooldownSpeedComponent.class
            );
    public static final ComponentKey<SpellPowerComponent> SPELL_POWER =
            ComponentRegistry.getOrCreate(
                    new Identifier(TheSpellLibrary.MOD_ID, "spell_power"),
                    SpellPowerComponent.class
            );
    public static final ComponentKey<CastSpeedComponent> CAST_SPEED =
            ComponentRegistry.getOrCreate(
                    new Identifier(TheSpellLibrary.MOD_ID, "cast_speed"),
                    CastSpeedComponent.class
            );


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                SCHOOLS_MANA,
                ManaComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );
        registry.registerForPlayers(
                MANA_REGEN,
                ManaRegenComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );registry.registerForPlayers(
                COOLDOWN_SPEED,
                CooldownSpeedComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );registry.registerForPlayers(
                SPELL_POWER,
                SpellPowerComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );
        registry.registerForPlayers(
                CAST_SPEED,
                CastSpeedComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );



    }
}
