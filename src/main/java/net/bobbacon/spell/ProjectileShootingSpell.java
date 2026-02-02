package net.bobbacon.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProjectileShootingSpell extends Spell{
    EntityType<FireballEntity> projectileType= EntityType.FIREBALL;
    public ProjectileShootingSpell(SpellType<? extends Spell> type, World world, LivingEntity user) {
        super(type, world, user);
    }

    @Override
    protected void cast(BlockPos pos) {
        if (!user.getWorld().isClient()){
            FireballEntity fireball= projectileType.create(user.getWorld());
            fireball.setPosition(user.getEyePos());
            fireball.setVelocity(user,user.getPitch(),user.getYaw(),0f,3,1f);
            user.getWorld().spawnEntity(fireball);
        }
        super.cast(pos);
    }
}
