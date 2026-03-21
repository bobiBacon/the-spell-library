package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class LargeProjectionSpell extends ConicalAreaSpell{
    final float power;
    public LargeProjectionSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, LargeProjectionSpell template) {
        super(type, world, user, template);
        power=template.power;
    }

    public LargeProjectionSpell(float range, float coneAngle, float power) {
        super(range, coneAngle);
        this.power = power;
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new LargeProjectionSpell(type,world,user,this);
    }

    @Override
    protected void apply(List<LivingEntity> entities) {
        project(entities,2,0.6);
    }

    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);
        shootParticles(ParticleTypes.SMOKE,true);
    }
}
