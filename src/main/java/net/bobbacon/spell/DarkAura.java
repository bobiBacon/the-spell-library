package net.bobbacon.spell;

import net.bobbacon.Accessors.WorldAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class DarkAura extends CircularAreaSpell implements TickedSpell{
    public DarkAura(SpellDef<? extends Spell> type, World world, LivingEntity user, DarkAura template) {
        super(type, world, user, template);
    }

    public DarkAura(float range) {
        super(range);
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new DarkAura(type,world,user,this);
    }

    @Override
    protected void apply(List<LivingEntity> entities) {
        for (LivingEntity entity:entities){
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,40,0));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,40,1));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER,40,0));
        }
    }

    @Override
    public boolean tick() {
        if (!world.isClient()){
            Vec3d origin= user.getPos();
            ((ServerWorld)world).spawnParticles(ParticleTypes.SQUID_INK,origin.x,origin.y,origin.z,200,range,1,range,0.01);
        }
        if (age%20==0){
            if (hasEnoughMana()){
                apply(targetEntities());
                consumeMana();

            }else {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMaxTime() {
        return 400;
    }

}
