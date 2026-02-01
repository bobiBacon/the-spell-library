package net.bobbacon.spell;


import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.TheSpellLibrary;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Spell {
    public final SpellType<? extends Spell> type;
    public final World world;
    public int remainingCooldown;
    public final LivingEntity user;

    public Spell(SpellType<? extends Spell> type, World world, LivingEntity user) {
        this.type = type;
        this.world = world;
        this.user = user;
        remainingCooldown= type.cooldown;
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
        TheSpellLibrary.LOGGER.info("is cooling down: "+ ((LivingEntityAccessor) user).the_spell_library$getSpellCooldowns().isCoolingDown(type));
        return type!=SpellTypes.EMPTY&&!((LivingEntityAccessor)user).the_spell_library$getSpellCooldowns().isCoolingDown(type);
    }

    public boolean isSingleUse(){
        return type.isSingleUse;
    }
    public int cooldownTime(){
        return type.cooldown;
    }
    public int remainingCooldownTime(){
        return remainingCooldown;
    }

}
