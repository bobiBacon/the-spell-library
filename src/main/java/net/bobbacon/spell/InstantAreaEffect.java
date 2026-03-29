package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class InstantAreaEffect extends CircularAreaSpell{
    public final StatusEffectInstance effect;
    public InstantAreaEffect(SpellDef<? extends Spell> type, World world, LivingEntity user, InstantAreaEffect template) {
        super(type, world, user, template);
        effect= template.effect;

    }

    public InstantAreaEffect(float range, StatusEffectInstance effect) {
        super(range);
        this.effect= effect;
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new InstantAreaEffect(type,world,user,this);
    }

    @Override
    protected void apply(List<LivingEntity> entities) {
        entities.forEach(entity -> {
            entity.addStatusEffect(new StatusEffectInstance(effect));
        });
        if (!world.isClient()){
            ((ServerWorld)world).spawnParticles(new DustParticleEffect(Vec3d.unpackRgb(getSchool().color).toVector3f(),2),user.getX(),user.getY()+0.5,user.getZ(),200,range,1,range,0);
        }
    }
}
