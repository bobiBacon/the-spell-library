package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;

public class FireBoom extends ProjectionSpell{
    protected FireBoom(SpellDef<? extends Spell> type, World world, LivingEntity user, FireBoom template) {
        super(type, world, user, template);
    }

    FireBoom(float range, DefaultParticleType particleType, float power, ParticleEffect projectionParticle) {
        super(range, particleType, power, projectionParticle);
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new FireBoom(type,world,user,this);
    }

    @Override
    protected void apply(LivingEntity entity) {
        super.apply(entity);
        entity.setOnFireFor(8);
        entity.damage(user.getDamageSources().create(DamageTypes.IN_FIRE,user),5);
    }
}
