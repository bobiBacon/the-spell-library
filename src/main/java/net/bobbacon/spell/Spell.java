package net.bobbacon.spell;


import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.Accessors.PlayerAccessor;
import net.bobbacon.TheSpellLibrary;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
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
        consumeMana();
    }
    public void consumeMana(){
        if (user instanceof PlayerEntity player){
            ((PlayerAccessor)player).the_spell_library$decrementMana(type.manaCost);
        }
    }
    public boolean hasEnoughMana(){
        if (user instanceof PlayerEntity player){
            return ((PlayerAccessor)player).the_spell_library$getMana()>=type.manaCost;
        }
        return true;
    }
    public boolean canCast(BlockPos pos){
        boolean enoughMana= hasEnoughMana();
        if (!enoughMana && user instanceof PlayerEntity player){
            player.sendMessage(Text.translatable("spell.the-spell-library.error.not_enough_mana"),true);
        }
        return type!=SpellTypes.EMPTY && !((LivingEntityAccessor)user).the_spell_library$getSpellCooldowns().isCoolingDown(type) && hasEnoughMana();
    }

    public boolean isSingleUse(){
        return type.isSingleUse;
    }
    public int cooldownTime(){
        return type.cooldown;
    }

}
