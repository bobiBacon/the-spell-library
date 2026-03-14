package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class ConicalAreaSpell extends AreaSpell{
    float coneAngle;
    public ConicalAreaSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, ConicalAreaSpell template) {
        super(type, world, user, template);
        coneAngle= template.coneAngle;
    }

    public ConicalAreaSpell(float range,float coneAngle) {
        super(range);
        this.coneAngle= coneAngle;
    }

    @Override
    public List<LivingEntity> targetEntities() {

        Vec3d origin = user.getCameraPosVec(1.0F);
        Vec3d look = user.getRotationVec(1.0F).normalize();
        Vec3d coneOrigin = origin.subtract(look);


        double cosHalfAngle = Math.cos(Math.toRadians(coneAngle / 2));

        Box searchBox = user.getBoundingBox().expand(range);

        List<LivingEntity> targets = user.getWorld().getEntitiesByClass(
                LivingEntity.class,
                searchBox,
                e -> e != user
        );
        targets.removeIf(entity -> {
            Vec3d toEntity = entity.getBoundingBox().getCenter().subtract(coneOrigin);
            Vec3d toEntityFromOrigin = entity.getBoundingBox().getCenter().subtract(origin);

            double distance = toEntity.length();

            if (distance > range+1) return true;

            Vec3d dir = toEntity.normalize();

            double dot = look.dotProduct(dir);
            double dot2= look.dotProduct(toEntityFromOrigin.normalize());

            return dot <= cosHalfAngle||dot2 < 0 ;
        });

        return targets;
    }


}
