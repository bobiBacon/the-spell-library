package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExampleSpell extends Spell{
    protected ExampleSpell(SpellDef<? extends Spell> type, World world, LivingEntity player, ExampleSpell template) {
        super(type, world, player, template);
    }

    ExampleSpell() {

    }


    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new ExampleSpell(type,world,user,this);
    }


    @Override
    protected void cast(BlockPos pos) {
        super.cast(pos);
        user.sendMessage(Text.of("This is an example spell"));
    }
}
