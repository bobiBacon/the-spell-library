package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class PunishmentSpell extends ConicalAreaSpell implements TickedSpell{
    public PunishmentSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, PunishmentSpell template) {
        super(type, world, user, template);
    }

    public PunishmentSpell(float range,float angle) {
        super(range,angle);
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new PunishmentSpell(type,world,user,this);
    }

    @Override
    protected void cast(BlockPos pos) {
        init();
        consumeMana();
        playReleasingSound(pos);
    }

    @Override
    public boolean tick() {
        if (age==10){
            apply(targetEntities());
        }
        return false;
    }

    @Override
    public int getMaxTime() {
        return 40;
    }

    @Override
    protected void apply(List<LivingEntity> entities) {
        entities.forEach(entity -> {entity.damage(user.getDamageSources().create(DamageTypes.MAGIC,user),8);});
    }
}
