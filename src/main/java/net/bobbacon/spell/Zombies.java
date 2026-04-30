package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Zombies extends Spell{
    public Zombies(SpellDef<? extends Spell> type, World world, LivingEntity user, Zombies template) {
        super(type, world, user, template);
    }

    public Zombies() {
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new Zombies(type,world,user,this);
    }

    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);
        for (int i = 0; i < 3; i++) {
            ZombieEntity zombie=new ZombieEntity(world);
            double x= (world.random.nextFloat()-0.5f)*3+user.getPos().getX();
            double y= user.getPos().getY();
            double z= (world.random.nextFloat()-0.5f)*3+user.getPos().getZ();
            zombie.setPos(x,y,z);
            world.spawnEntity(zombie);
        }
    }
}
