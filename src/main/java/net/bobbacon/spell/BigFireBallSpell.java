package net.bobbacon.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BigFireBallSpell extends ProjectileShootingSpell{
    protected BigFireBallSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, ProjectileShootingSpell template) {
        super(type, world, user, template);
    }

    BigFireBallSpell() {
        super(EntityType.FIREBALL);
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new BigFireBallSpell(type,world,user,this);
    }

    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);
        if (!world.isClient()){
            Vec3d vec3d = user.getRotationVector();
            FireballEntity entity= new FireballEntity(world,user,vec3d.getX(),vec3d.getY(),vec3d.getZ(),12);
            entity.setVelocity(user,user.getPitch(),user.getYaw(),user.getRoll(),1.5f,1);
            user.getWorld().spawnEntity(entity);
            entity.setPosition(user.getEyePos());
        }
    }
}
