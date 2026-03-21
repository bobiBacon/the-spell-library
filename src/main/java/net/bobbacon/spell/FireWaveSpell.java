package net.bobbacon.spell;

import net.bobbacon.particles.ModParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FireWaveSpell extends ConicalAreaSpell{
    public FireWaveSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, ConicalAreaSpell template) {
        super(type, world, user, template);
    }

    public FireWaveSpell(float range, float coneAngle) {
        super(range, coneAngle);
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new FireWaveSpell(type,world,user,this);
    }

    @Override
    protected void apply(List<LivingEntity> entities) {
        for (LivingEntity entity: entities){
            entity.damage(user.getDamageSources().create(DamageTypes.IN_FIRE,user),8);
            entity.setOnFireFor(5);
        }
        project(entities,0.7,0.8);
    }

    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);
        shootParticles(ModParticles.FIRE_PARTICLE,false);

    }
}
