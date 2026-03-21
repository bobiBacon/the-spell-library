package net.bobbacon.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SonicBoomParticle;
import net.minecraft.client.particle.SpellParticle;

public class ModParticlesClient {

    public static void init(){
        ParticleFactoryRegistry.getInstance().register(ModParticles.WIND_PROJECTION_PARTICLE, SonicBoomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FIRE_PARTICLE, FireParticle.Factory::new);
    }
}
