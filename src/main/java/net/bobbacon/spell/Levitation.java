package net.bobbacon.spell;

import net.bobbacon.Accessors.WorldAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Levitation extends SelfEffectSpell implements TickedSpell{
    public Levitation(SpellDef<? extends Spell> type, World world, LivingEntity user, Levitation template) {
        super(type, world, user, template);
    }

    public Levitation() {
        super(new StatusEffectInstance(StatusEffects.LEVITATION,2,5));
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new Levitation(type,world,user,this);
    }

    @Override
    protected void cast(BlockPos pos) {
        init();
        playReleasingSound(pos);
    }

    @Override
    public boolean tick() {
        user.addStatusEffect(new StatusEffectInstance(effects.get(0)));
        consumeMana(1f/80);
        return false;
    }

    @Override
    public int getMaxTime() {
        return 80;
    }

    @Override
    public void stopCasting() {
        super.stopCasting();
        if (casted){
            applyCooldown();
        WorldAccessor world = (WorldAccessor) (Object) this.world;
        world.getSpellTickingManager().removeSpell(this);
        }
    }
}
