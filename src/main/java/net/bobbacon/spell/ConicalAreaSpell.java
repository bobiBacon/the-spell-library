package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class ConicalAreaSpell extends AreaSpell{
    float coneAngle;
    public ConicalAreaSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, ConicalAreaSpell template) {
        super(type, world, user, template);
        coneAngle= template.coneAngle;
    }

    public ConicalAreaSpell(float range,float coneAngle) {
        super(range);
        this.coneAngle= coneAngle;
    }

    @Override
    public List<LivingEntity> targetEntities() {

        Vec3d origin = user.getCameraPosVec(1.0F);
        Vec3d look = user.getRotationVec(1.0F).normalize();
        Vec3d coneOrigin = origin.subtract(look);


        double cosHalfAngle = Math.cos(Math.toRadians(coneAngle / 2));

        Box searchBox = user.getBoundingBox().expand(range);

        List<LivingEntity> targets = user.getWorld().getEntitiesByClass(
                LivingEntity.class,
                searchBox,
                e -> e != user
        );
        targets.removeIf(entity -> {
            Vec3d toEntity = entity.getBoundingBox().getCenter().subtract(coneOrigin);
            Vec3d toEntityFromOrigin = entity.getBoundingBox().getCenter().subtract(origin);

            double distance = toEntity.length();

            if (distance > range+1) return true;

            Vec3d dir = toEntity.normalize();

            double dot = look.dotProduct(dir);
            double dot2= look.dotProduct(toEntityFromOrigin.normalize());

            return dot <= cosHalfAngle||dot2 < 0 ;
        });

        return targets;
    }
    public void shootParticles(ParticleEffect particle, boolean head){
        Vec3d look = user.getRotationVec(1.0F).normalize();

        Vec3d origin = head? user.getEyePos().subtract(look):user.getPos().subtract(look);

        Vec3d right = look.crossProduct(new Vec3d(0,1,0)).normalize();
        Vec3d up = right.crossProduct(look).normalize();

        for (int i = (int)(-coneAngle / 2); i < coneAngle / 2; i+=2) {

            float angle = (float)Math.toRadians(i);

            Vec3d dir =
                    look
                            .add(right.multiply(Math.sin(angle)))
                            .normalize()
                            .multiply(0.6);

            world.addParticle(
                    particle,
                    origin.x + world.random.nextFloat()*look.x,
                    origin.y + world.random.nextFloat() * 1.5f* (head?-1:1),
                    origin.z + world.random.nextFloat()*look.z,
                    dir.x,
                    dir.y,
                    dir.z
            );
        }
    }

}
