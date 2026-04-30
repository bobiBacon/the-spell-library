package net.bobbacon.spell;


import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.Accessors.PlayerAccessor;
import net.bobbacon.Accessors.WorldAccessor;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.components.ManaComponent;
import net.bobbacon.components.ModComponents;
import net.bobbacon.components.SpellsStatApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Spell {
    public final SpellDef<? extends Spell> type;
    public final World world;
    public final LivingEntity user;
    public int age=0;
    public Vec3d pos;
    public Vec3d orientation;
    public final static UUID COMPLEX_ANIMATION_SLOT= UUID.fromString("0596b76c-356c-41e0-9f81-374c0f4bd0d3");

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
        init();
        consumeMana();
        playReleasingSound(pos);
        applyCooldown();
    }
    protected void applyCooldown(){
        ((LivingEntityAccessor)user).the_spell_library$getSpellCooldowns()

                .set(this.type, this.getCooldownTime());
    }
    protected void init(){
        this.pos=user.getPos();
        this.orientation= user.getRotationVector().normalize();
        if (this instanceof TickedSpell){
            WorldAccessor world = (WorldAccessor) (Object) this.world;
            world.getSpellTickingManager().addSpell((TickedSpell) this);
        }    }
    public void consumeMana(){
        consumeMana(1);
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
    public int getCooldownTime(){
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
            float toConsume=type.manaCost*modifier;
            Map<SpellSchool, Mana> manaMap = Mana.getSchoolsMana(player);
            Mana mana=manaMap.get(getSchool());
            float manaPoints = mana.getTotalMana();
            if (toConsume>= manaPoints && manaPoints >0){
                toConsume-= manaPoints;
                mana.useMana(manaPoints);
            } else if (toConsume < manaPoints) {
                mana.useMana(toConsume);
                toConsume=0;
            }
            ((PlayerAccessor)player).the_spell_library$decrementMana(toConsume);
        }
    }
    public SpellSchool getSchool(){
        return this.type.school;
    }
    public List<Text> getTooltips(){
        Style style= getSchool().style;
        ArrayList<Text> list= new ArrayList<>();
        list.add(Text.translatable("spell.the-spell-library.tooltip.mana",this.type.manaCost).setStyle(style));
        list.add(Text.translatable("spell.the-spell-library.tooltip.cast_time",this.type.getCastTime(user)/20.0).setStyle(style));
        if (this.isSingleUse()){
            list.add(Text.translatable("spell.the-spell-library.tooltip.single_use").setStyle(style));
        }else {
            list.add(Text.translatable("spell.the-spell-library.tooltip.cooldown",this.type.cooldown/20.0).setStyle(style));
        }
        return list;
    }
    public void stopCasting(){
        if (user instanceof PlayerEntity player){
            ((PlayerAccessor)player).setCurrentlyCastingSpell(null);
            Identifier animationId = new Identifier(TheSpellLibrary.MOD_ID, "spell_casting_test");
            if (type==SpellDefs.Levitation){
                animationId = new Identifier(TheSpellLibrary.MOD_ID, "levitation_cast");
            }
            TheSpellLibrary.stopComplexAnimation(player,COMPLEX_ANIMATION_SLOT);
        }
    }

    public int getConcentrationTime() {
        return type.getConcentrationTime();
    }
    public int getCastTime(LivingEntity user){
        return type.getCastTime(user);
    }
    public float getSpellPower(){
        if (user instanceof PlayerEntity player){
            return SpellsStatApi.getStat(SpellsStatApi.getSpellPowerComponent(player),getSchool())*SpellsStatApi.getStat(SpellsStatApi.getSpellPowerComponent(player));
        }
        else return 1;
    }

}
