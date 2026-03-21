package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
    ParticleEffect particleType;

    protected TargetingSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, TargetingSpell template) {
        super(type, world, user,template);
        this.range = template.range;
        this.particleType= template.particleType;
    }
    TargetingSpell(float range, ParticleEffect particleType) {
        super();
        this.range = range;
        this.particleType= particleType;
    }



    @Override
    protected void cast(BlockPos pos) {


        EntityHitResult result = targetEntity();

        if (result != null) {
            apply((LivingEntity) result.getEntity());
            super.cast(pos);
        }
        else {
            fail(pos);
        }
    }
    public EntityHitResult targetEntity(){
        Vec3d start = user.getCameraPosVec(1.0F);
        Vec3d direction = user.getRotationVec(1.0F);

        Vec3d end = start.add(direction.multiply(range));

        Box box = user.getBoundingBox().stretch(direction.multiply(range)).expand(1.5);
        return raycastWithLease(
                user,
                start,
                end,
                box,
                entity -> {



                    return entity instanceof LivingEntity&&!entity.isSpectator() && entity.canHit()&&user.canSee(entity);},
                range * range
        );
    }
    @Nullable
    protected static EntityHitResult raycastWithLease(Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double d) {
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

    @Override
    public void castingTick(BlockPos pos, int remainingTicks) {
        if ((remainingTicks&11)==0){
            EntityHitResult hitResult= targetEntity();
            if (hitResult!=null){
                Entity entity =hitResult.getEntity();
                for (int i = 0; i < 8; i++) {
                    world.addParticle(particleType,entity.getParticleX(1),entity.getRandomBodyY(),entity.getParticleZ(1),entity.getVelocity().getX(),entity.getVelocity().getY(),entity.getVelocity().getZ());
                }
            }
        }
    }

    @Override
    public List<Text> getTooltips() {
        List<Text> list= super.getTooltips();
        list.add(Text.translatable("spell.the-spell-library.tooltip.range",range).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
        return list;
    }
}
