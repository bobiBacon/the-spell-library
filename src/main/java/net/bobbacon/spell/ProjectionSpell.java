package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectionSpell extends TargetingSpell{
    final float power;
    final ParticleEffect projectionParticle;
    protected ProjectionSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, ProjectionSpell template) {
        super(type, world, user, template);
        power= template.power;
        projectionParticle =template.projectionParticle;
    }

    ProjectionSpell(float range, DefaultParticleType particleType, float power, ParticleEffect projectionParticle) {
        super(range, particleType);
        this.power = power;
        this.projectionParticle = projectionParticle;
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new ProjectionSpell(type,world,user,this);
    }

    @Override
    protected void apply(LivingEntity entity) {

        SpellUtils.project(entity,power,0.5,user);
        Vec3d userPos= user.getBoundingBox().getCenter();
        Vec3d entityPos= entity.getEyePos();
        Vec3d toEntity= entityPos.subtract(userPos);
        Vec3d look= toEntity.normalize();
        if (!world.isClient()){
            ServerWorld serverWorld=(ServerWorld) world;
            for (int i = 1; i < MathHelper.floor(toEntity.length()) + 7; i++) {
                Vec3d vec3d4 = userPos.add(look.multiply(i));
                serverWorld.spawnParticles(projectionParticle, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }

    }

    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);


    }
}
