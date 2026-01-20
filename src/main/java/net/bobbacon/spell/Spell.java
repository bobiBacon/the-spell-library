package net.bobbacon.spell;


import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Spell {
    public final SpellType<? extends Spell> type;
    public final World world;
    public final LivingEntity user;

    public Spell(SpellType<? extends Spell> type, World world, LivingEntity user) {
        this.type = type;
        this.world = world;
        this.user = user;
    }

    public boolean casted= false;
    public boolean tryCast(BlockPos pos){
        casted= canCast(pos);
        if (casted)
            cast(pos);
        return casted;
    }
    protected void cast(BlockPos pos){

    }
    public boolean canCast(BlockPos pos){
        return false;
    }

    public boolean isSingleUse(){
        return false;
    }

}
