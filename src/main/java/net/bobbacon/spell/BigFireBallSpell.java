package net.bobbacon.spell;

import net.bobbacon.Accessors.WorldAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BigFireBallSpell extends ProjectileShootingSpell implements TickedSpell{
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
        user.addVelocity(0,2.5,0);
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,200,0,false,false));
        if (!world.isClient){
            WorldAccessor serverWorld= (WorldAccessor) (Object)world;
            serverWorld.getSpellTickingManager().addSpell(this);
        }
        consumeMana();
    }
    protected void shoot(){
        if (!world.isClient()){
            Vec3d vec3d = user.getRotationVector();
            FireballEntity entity= new FireballEntity(world,user,vec3d.getX(),vec3d.getY(),vec3d.getZ(),12);
            entity.setVelocity(user,user.getPitch(),user.getYaw(),user.getRoll(),1.5f,1);
            user.getWorld().spawnEntity(entity);
            entity.setPosition(user.getEyePos());
            playReleasingSound(user.getBlockPos().up());
        }
    }
    int angle=0;
    int angle2=120;
    int angle3=240;
    @Override
    public boolean tick() {
        angle++;
        angle2++;
        angle3++;
        if ((age&1)==0&&world instanceof ServerWorld serverWorld){
            serverWorld.spawnParticles(ParticleTypes.FLAME,Math.cos(angle)+user.getX(),user.getEyeY(),Math.sin(angle)+user.getZ(),1,0,1,0,0);
            serverWorld.spawnParticles(ParticleTypes.FLAME,Math.cos(angle2)+user.getX(),user.getEyeY(),Math.sin(angle2)+user.getZ(),1,0,1,0,0);
            serverWorld.spawnParticles(ParticleTypes.FLAME,Math.cos(angle3)+user.getX(),user.getEyeY(),Math.sin(angle3)+user.getZ(),1,0,1,0,0);
        }
        if (age==60){
            shoot();
        }
        return false;
    }

    @Override
    public int getMaxTime() {
        return 60;
    }


}
