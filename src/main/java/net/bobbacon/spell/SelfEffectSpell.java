package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class SelfEffectSpell extends Spell{
    public final List<StatusEffectInstance> effects;
    public SelfEffectSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, SelfEffectSpell template) {
        super(type, world, user, template);
        effects=template.effects;
    }

    public SelfEffectSpell(List<StatusEffectInstance> effects) {
        this.effects = effects;
    }
    public SelfEffectSpell(StatusEffectInstance... effects) {
        this.effects = List.of(effects);
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new SelfEffectSpell(type,world,user,this);
    }

    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);
        effects.forEach(effect -> user.addStatusEffect(new StatusEffectInstance(effect)));
    }
}
