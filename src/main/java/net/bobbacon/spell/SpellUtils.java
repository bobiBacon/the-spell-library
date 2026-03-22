package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SpellUtils {
    public static void project(LivingEntity entity,double power, double heightFactor,LivingEntity user){
        if (entity.getWorld().isClient()) return;
        Vec3d origin = user.getCameraPosVec(1.0F);
        Vec3d toEntity = entity.getBoundingBox().getCenter().subtract(origin);
        Vec3d v = toEntity.normalize();

        Vec3d up = new Vec3d(0,1,0);

        Vec3d perp = up.subtract(v.multiply(up.dotProduct(v))).normalize().multiply(heightFactor);
        entity.addVelocity(v.add(perp).normalize().multiply(power));
        if (entity instanceof ServerPlayerEntity player) {
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        }
    }
}
