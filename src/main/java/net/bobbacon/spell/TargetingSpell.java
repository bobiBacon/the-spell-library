package net.bobbacon.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class TargetingSpell extends Spell{
    public float range;
    protected TargetingSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, TargetingSpell template) {
        super(type, world, user,template);
        this.range = template.range;
    }
    TargetingSpell(float range) {
        super();
        this.range = range;
    }



    @Override
    protected void cast(BlockPos pos) {
        Vec3d start = user.getCameraPosVec(1.0F);
        Vec3d direction = user.getRotationVec(1.0F);

        Vec3d end = start.add(direction.multiply(range));

        Box box = user.getBoundingBox().stretch(direction.multiply(range)).expand(1.5);

//        double radius = 1.5;
//
//        List<LivingEntity> entities = world.getEntitiesByClass(
//                LivingEntity.class,
//                box,
//                e -> e != user
//        );
//
//        for (LivingEntity entity : entities) {
//            Vec3d closest = entity.getBoundingBox().getCenter();
//            double dist = closest.squaredDistanceTo(start);
//
//            if (dist < radius * radius) {
//                apply(entity);
//                super.cast(pos);
//                return;
//            }
//        }
//        fail(pos);
        EntityHitResult result = raycast(
                user,
                start,
                end,
                box,
                entity -> {
                    Vec3d start2 = user.getCameraPosVec(1.0F);
                    Vec3d end2 = entity.getEyePos();

                    BlockHitResult blockHit = user.getWorld().raycast(new RaycastContext(
                            start2,
                            end2,
                            RaycastContext.ShapeType.COLLIDER,
                            RaycastContext.FluidHandling.NONE,
                            user
                    ));

                    boolean visible = blockHit.getType() == HitResult.Type.MISS;

                    return entity instanceof LivingEntity&&!entity.isSpectator() && entity.canHit()&&visible;},
                range * range
        );

        if (result != null) {
            apply((LivingEntity) result.getEntity());
            super.cast(pos);
        }
        else {
            fail(pos);
        }
    }
    @Nullable
    public static EntityHitResult raycast(Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double d) {
        World world = entity.getWorld();
        double e = d;
        Entity entity2 = null;
        Vec3d vec3d = null;

        for (Entity entity3 : world.getOtherEntities(entity, box, predicate)) {
            Box box2 = entity3.getBoundingBox().expand(entity3.getTargetingMargin()).expand(0.3);
            Optional<Vec3d> optional = box2.raycast(min, max);
            if (box2.contains(min)) {
                if (e >= 0.0) {
                    entity2 = entity3;
                    vec3d = optional.orElse(min);
                    e = 0.0;
                }
            } else if (optional.isPresent()) {
                Vec3d vec3d2 = optional.get();
                double f = min.squaredDistanceTo(vec3d2);
                if (f < e || e == 0.0) {
                    if (entity3.getRootVehicle() == entity.getRootVehicle()) {
                        if (e == 0.0) {
                            entity2 = entity3;
                            vec3d = vec3d2;
                        }
                    } else {
                        entity2 = entity3;
                        vec3d = vec3d2;
                        e = f;
                    }
                }
            }
        }

        return entity2 == null ? null : new EntityHitResult(entity2, vec3d);
    }

    protected abstract void apply(LivingEntity entity);
}
