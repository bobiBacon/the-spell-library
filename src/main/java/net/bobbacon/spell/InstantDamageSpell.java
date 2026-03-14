package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InstantDamageSpell extends TargetingSpell{
    float damage;
    RegistryKey<DamageType> damageType;
    protected InstantDamageSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, InstantDamageSpell template) {
        super(type, world, user, template);
        damage=template.damage;
        this.damageType= template.damageType;

    }

    InstantDamageSpell(float range, float damage, RegistryKey<DamageType> damageType, DefaultParticleType particleType) {
        super(range,particleType);
        this.damage=damage;
        this.damageType= damageType;
    }


    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new InstantDamageSpell(type,world,user,this);
    }

    @Override
    protected void apply(LivingEntity entity) {
        entity.damage(user.getDamageSources().create(damageType,user),damage);
        if (!world.isClient){
            ((ServerWorld)world).spawnParticles(particleType,entity.getX(),entity.getEyeY(),entity.getZ(),15,0,0,0,0.1);
        }
    }

}
