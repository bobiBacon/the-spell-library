package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FireWaveSpell extends ConicalAreaSpell{
    public FireWaveSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, ConicalAreaSpell template) {
        super(type, world, user, template);
    }

    public FireWaveSpell(float range, float coneAngle) {
        super(range, coneAngle);
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new FireWaveSpell(type,world,user,this);
    }

    @Override
    protected void apply(List<LivingEntity> entities) {
        project(entities,0.7,0.8);
    }

    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);
//        for (int i = (int) (-coneAngle/2); i < coneAngle/2; i++) {
//            for (int j = 0; j < 6; j++) {
//                Vec3d vec= user.getRotationVector().rotateY((float) Math.toRadians(i)).add(0,world.random.nextFloat()*0.5,0).multiply(0.3);
//                world.addParticle(ParticleTypes.FLAME,user.getX(),user.getY()+world.random.nextFloat()*1.5f,user.getZ(),vec.getX(),vec.getY(),vec.getZ());
//            }
//
//        }



//        Vec3d look = user.getRotationVec(1.0F).normalize();
//
//// vecteur horizontal perpendiculaire
//        Vec3d right = look.crossProduct(new Vec3d(0,1,0)).normalize();
//
//// vecteur vertical du cône
//        Vec3d up = right.crossProduct(look).normalize();
//
//        for (int i = (int)(-coneAngle / 2); i < coneAngle / 2; i++) {
//
//            float angle = (float)Math.toRadians(i);
//
//            Vec3d dir =
//                    look
//                            .add(right.multiply(Math.sin(angle)))
//                            .add(up.multiply(0))
//                            .normalize()
//                            .multiply(0.6);
//
//            world.addParticle(
//                    ParticleTypes.FLAME,
//                    user.getX()+world.random.nextFloat()*look.x,
//                    user.getY() + world.random.nextFloat() * 1.5f,
//                    user.getZ()+world.random.nextFloat()*look.z,
//                    dir.x,
//                    dir.y,
//                    dir.z
//            );
//        }
        Vec3d look = user.getRotationVec(1.0F).normalize();

// origine 1 bloc derrière le joueur
        Vec3d origin = user.getPos().subtract(look);

// vecteurs du repère local
        Vec3d right = look.crossProduct(new Vec3d(0,1,0)).normalize();
        Vec3d up = right.crossProduct(look).normalize();

        for (int i = (int)(-coneAngle / 2); i < coneAngle / 2; i++) {

            float angle = (float)Math.toRadians(i);

            Vec3d dir =
                    look
                            .add(right.multiply(Math.sin(angle)))
                            .normalize()
                            .multiply(0.6);

            world.addParticle(
                    ParticleTypes.FLAME,
                    origin.x + world.random.nextFloat()*look.x,
                    origin.y + world.random.nextFloat() * 1.5f,
                    origin.z + world.random.nextFloat()*look.z,
                    dir.x,
                    dir.y,
                    dir.z
            );
        }

//        for (float yaw = -coneAngle/2; yaw < coneAngle/2; yaw += 5) {
//            for (float pitch = -coneAngle/4; pitch < coneAngle/4; pitch += 5) {
//
//                Vec3d dir = look
//                        .rotateY((float)Math.toRadians(yaw))
//                        .rotateX((float)Math.toRadians(pitch))
//                        .multiply(0.6);
//
//                world.addParticle(
//                        ParticleTypes.FLAME,
//                        user.getX(),
//                        user.getEyeY(),
//                        user.getZ(),
//                        dir.x, dir.y, dir.z
//                );
//            }
//        }
    }
}
