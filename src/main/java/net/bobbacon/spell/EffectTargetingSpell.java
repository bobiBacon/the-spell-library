package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;

public class EffectTargetingSpell extends TargetingSpell{
    StatusEffectInstance effect;
    protected EffectTargetingSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, EffectTargetingSpell template) {
        super(type, world, user, template);
        effect= template.effect;
    }

    EffectTargetingSpell(float range, DefaultParticleType particleType, StatusEffectInstance effect) {
        super(range, particleType);
        this.effect=effect;
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new EffectTargetingSpell(type,world,user,this);
    }

    @Override
    protected void apply(LivingEntity entity) {
        if (!world.isClient){
            entity.addStatusEffect(new StatusEffectInstance(effect));
        }
    }
}
