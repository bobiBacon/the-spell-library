package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DashSpell extends Spell{
    protected final float distance;
    protected DashSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, DashSpell template) {
        super(type, world, user, template);
        distance= template.distance;
    }

    protected DashSpell(float distance) {
        this.distance = distance;
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new DashSpell(type,world,user,this);
    }

    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);
        user.setPos(user.getX(),user.getY()+0.05,user.getZ());
        user.setOnGround(false);
        user.addVelocity(user.getRotationVector().normalize().multiply(distance));
    }
}
