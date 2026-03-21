package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class CircularAreaSpell extends AreaSpell{
    public CircularAreaSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, AreaSpell template) {
        super(type, world, user, template);
    }

    public CircularAreaSpell(float range) {
        super(range);
    }

    @Override
    public List<LivingEntity> targetEntities() {
        Vec3d origin=user.getBoundingBox().getCenter();
        Box searchBox = user.getBoundingBox().expand(range);
        List<LivingEntity> targets = user.getWorld().getEntitiesByClass(
                LivingEntity.class,
                searchBox,
                e -> {
                    double distance= origin.distanceTo(e.getBoundingBox().getCenter());
                    return e != user && distance<=range;
                }
        );
        return targets;
    }
}
