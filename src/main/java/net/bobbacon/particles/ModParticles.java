package net.bobbacon.particles;

import net.bobbacon.TheSpellLibrary;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static final DefaultParticleType WIND_PROJECTION_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType FIRE_PARTICLE = FabricParticleTypes.simple();
    public static void init(){
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TheSpellLibrary.MOD_ID, "wind_projection_particle"), WIND_PROJECTION_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TheSpellLibrary.MOD_ID, "rolling_fire_particle"), FIRE_PARTICLE);
    }
}
