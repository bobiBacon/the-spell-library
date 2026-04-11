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

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                SCHOOLS_MANA,
                ManaComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );
    }
}
