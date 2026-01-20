package net.bobbacon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExampleSpell extends Spell{
    public ExampleSpell(SpellType<? extends Spell> type, World world, LivingEntity player) {
        super(type, world, player);
    }

    @Override
    protected void cast(BlockPos pos) {
        user.sendMessage(Text.of("This is an example spell"));
    }
    @Override
    public boolean canCast(BlockPos pos) {
        return true;
    }
}
