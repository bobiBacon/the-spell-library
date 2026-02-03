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

public class ProjectileShootingSpell extends Spell{
    public EntityType<? extends Entity> projectileType;
    protected ProjectileShootingSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, ProjectileShootingSpell template) {
        super(type, world, user,template);
        this.projectileType = template.projectileType;
    }
    ProjectileShootingSpell(EntityType<? extends Entity> projectileType) {
        super();
        this.projectileType = projectileType;
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new ProjectileShootingSpell(type,world,user,this);
    }

    @Override
    protected void cast(BlockPos pos) {
        if (!user.getWorld().isClient()){
            Entity entity= projectileType.create(user.getWorld());
            Vec3d vec3d = user.getRotationVector();


            if (entity instanceof ProjectileEntity projectile){

                if (projectile instanceof ExplosiveProjectileEntity explosiveProjectileEntity){
                    explosiveProjectileEntity.powerX = vec3d.x * 0.1;
                    explosiveProjectileEntity.powerY = vec3d.y * 0.1;
                    explosiveProjectileEntity.powerZ = vec3d.z * 0.1;
                    explosiveProjectileEntity.setOwner(user);
                }else {
                    projectile.setVelocity(user,user.getPitch(),user.getYaw(),0f,3,1f);
                }
            }else {
                vec3d=vec3d.multiply(3.0);
            }
            entity.setPosition(user.getEyePos());
            entity.setVelocity(vec3d);
            user.getWorld().spawnEntity(entity);

        }
        super.cast(pos);
    }
}
