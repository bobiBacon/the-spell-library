package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class AreaSpell extends Spell{
    float range;
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
            entity.damage(user.getDamageSources().create(DamageTypes.IN_FIRE,user),8);
            entity.setOnFireFor(5);

            Vec3d origin = user.getCameraPosVec(1.0F);
            Vec3d toEntity = entity.getBoundingBox().getCenter().subtract(origin);
            Vec3d v = toEntity.normalize();

            Vec3d up = new Vec3d(0,1,0);

            Vec3d perp = up.subtract(v.multiply(up.dotProduct(v))).normalize().multiply(heightFactor);

            entity.addVelocity(v.add(perp).normalize().multiply(power));
        }
    }
}
