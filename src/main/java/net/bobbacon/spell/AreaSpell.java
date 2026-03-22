package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class AreaSpell extends Spell{
    public float range;
    public AreaSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, AreaSpell template) {
        super(type, world, user, template);
        range= template.range;
    }

    public AreaSpell(float range) {
        this.range=range;
    }
    public abstract List<LivingEntity> targetEntities();
    protected abstract void apply(List<LivingEntity> entities);

    @Override
    protected void cast(BlockPos pos) {
        apply(targetEntities());
        super.cast(pos);

    }
    public void project(List<LivingEntity> entities,double power, double heightFactor){
        for (LivingEntity entity: entities){
            SpellUtils.project(entity,power,heightFactor,user);
        }
    }
}
