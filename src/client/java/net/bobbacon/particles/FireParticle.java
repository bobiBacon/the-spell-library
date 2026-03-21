package net.bobbacon.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class FireParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected FireParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, h, i);
        this.setSpriteForAge(spriteProvider);
        this.maxAge = 30;
        this.scale = 1.5F;
        this.spriteProvider = spriteProvider;
        this.velocityMultiplier = 0.96F;
        this.velocityX = this.velocityX * 0.01F + g;
        this.velocityY = this.velocityY * 0.01F + h;
        this.velocityZ = this.velocityZ * 0.01F + i;
        this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;

    }
    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }
    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
            this.velocityY = this.velocityY - 0.04 * this.gravityStrength;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            if (this.ascending && this.y == this.prevPosY) {
                this.velocityX *= 1.1;
                this.velocityZ *= 1.1;
            }

            this.velocityX = this.velocityX * this.velocityMultiplier;
            this.velocityY = this.velocityY * this.velocityMultiplier;
            this.velocityZ = this.velocityZ * this.velocityMultiplier;
            if (this.onGround) {
                this.velocityX *= 0.7F;
                this.velocityZ *= 0.7F;
            }
        }
    }
    public int getBrightness(float tint) {
        return 15728880;
    }


    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new FireParticle(clientWorld, d, e, f,g,h,i, this.spriteProvider);
        }
    }
}
