package net.bobbacon.spell;


import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.Accessors.PlayerAccessor;
import net.bobbacon.Accessors.WorldAccessor;
import net.bobbacon.TheSpellLibrary;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Spell {
    public final SpellDef<? extends Spell> type;
    public final World world;
    public final LivingEntity user;
    public int age=0;


    protected Spell(SpellDef<? extends Spell> type, World world, LivingEntity user, Spell template) {
        this.type = type;
        this.world = world;
        this.user = user;

    }
    protected Spell() {
        this.type = null;
        world=null;
        user=null;
    }
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new Spell(type,world,user,this);
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
        playReleasingSound(pos);
        if (!world.isClient&&this instanceof TickedSpell spell){
            WorldAccessor serverWorld= (WorldAccessor) (Object)world;
            serverWorld.getSpellTickingManager().addSpell(spell);
        }
    }
    public void consumeMana(){
        if (user instanceof PlayerEntity player&&!player.isCreative()){
            ((PlayerAccessor)player).the_spell_library$decrementMana(type.manaCost);
        }
    }
    public boolean hasEnoughMana(){
        if (user instanceof PlayerEntity player){
            return ((PlayerAccessor)player).the_spell_library$getMana()>=type.manaCost||player.isCreative();
        }
        return true;
    }
    public boolean canCast(BlockPos pos){
        boolean enoughMana= hasEnoughMana();
        if (!enoughMana && user instanceof PlayerEntity player){
            player.sendMessage(Text.translatable("spell.the-spell-library.error.not_enough_mana"),true);
        }
        return type!= SpellDefs.EMPTY && !((LivingEntityAccessor)user).the_spell_library$getSpellCooldowns().isCoolingDown(type) && hasEnoughMana();
    }
    public void castingTick(BlockPos pos,int remainingTicks){
        if (user instanceof PlayerEntity){
            ((PlayerAccessor)user).setCurrentlyCastingSpell(this);
        }
    }

    public boolean isSingleUse(){
        return type.isSingleUse;
    }
    public int cooldownTime(){
        return type.cooldown;
    }
    public void playCastingSound(BlockPos pos){
        playCastingSound(pos, type.castingSound);
    }
    public void playCastingSound(BlockPos pos, SoundEvent soundEvent){
        if (soundEvent!=null){
            world.playSound(null,pos, soundEvent, user instanceof PlayerEntity? SoundCategory.PLAYERS:SoundCategory.HOSTILE,world.random.nextFloat()*0.4f+0.5f,0.6f+world.random.nextFloat()*0.8f);
        }
    }
    public void playReleasingSound(BlockPos pos){
        playReleasingSound(pos,type.releasingSound);
    }
    public void playReleasingSound(BlockPos pos, SoundEvent soundEvent){
        if (soundEvent!=null){
            world.playSound(null,pos, soundEvent, user instanceof PlayerEntity? SoundCategory.PLAYERS:SoundCategory.HOSTILE,world.random.nextFloat()*0.4f+0.5f,0.6f+world.random.nextFloat()*0.8f);
        }
    }
    public void fail(BlockPos pos){
        consumeMana(0.5f);
    }
    public void consumeMana(float modifier){
        if (user instanceof PlayerEntity player&&!player.isCreative()){
            ((PlayerAccessor)player).the_spell_library$decrementMana(type.manaCost*modifier);
        }
    }
    public SpellSchool getSchool(){
        return this.type.school;
    }
    public List<Text> getTooltips(){
        Style style= getSchool().style;
        ArrayList<Text> list= new ArrayList<>();
        list.add(Text.translatable("spell.the-spell-library.tooltip.mana",this.type.manaCost).setStyle(style));
        list.add(Text.translatable("spell.the-spell-library.tooltip.cast_time",this.type.castTime/20.0).setStyle(style));
        if (this.isSingleUse()){
            list.add(Text.translatable("spell.the-spell-library.tooltip.single_use").setStyle(style));
        }else {
            list.add(Text.translatable("spell.the-spell-library.tooltip.cooldown",this.type.cooldown/20.0).setStyle(style));
        }
        return list;
    }
    public void abort(){
        if (user instanceof PlayerEntity player){
            ((PlayerAccessor)player).setCurrentlyCastingSpell(null);
        }
    }
}
