package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class SpellUtils {
    public static void project(LivingEntity entity,double power, double heightFactor,LivingEntity user){
        Vec3d origin = user.getCameraPosVec(1.0F);
        Vec3d toEntity = entity.getBoundingBox().getCenter().subtract(origin);
        Vec3d v = toEntity.normalize();

        Vec3d up = new Vec3d(0,1,0);

        Vec3d perp = up.subtract(v.multiply(up.dotProduct(v))).normalize().multiply(heightFactor);

        entity.addVelocity(v.add(perp).normalize().multiply(power));
    }
}
