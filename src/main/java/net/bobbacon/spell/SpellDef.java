package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

//defines properties of a spell
public class  SpellDef<T extends Spell> {


    public boolean creativeTab= true;
    public boolean isSingleUse= true;
    public Identifier customTextureId= null;
    public int cooldown= 0;
    public int castTime = 20;
    public float manaCost=0;
    Identifier tintedTexture2d = null;
    final ArrayList<Integer> tints= new ArrayList<>();
    private final Spell template;
    public SoundEvent castingSound= ModSounds.DEFAULT_CASTING;
    public SoundEvent releasingSound=ModSounds.DEFAULT_RELEASING;
    public boolean usesDefaultLootTable= true;
    public Rarity rarity= Rarity.COMMON;

    public SpellDef(Spell template, float manaCost) {
        this.manaCost= manaCost;
        this.template = template;
    }
    public SpellDef<? extends Spell> hideInCreativeTab(){
        creativeTab=false;
        return this;
    }
    public SpellDef<? extends Spell> notInDefaultLootTable(){
        usesDefaultLootTable=false;
        return this;
    }
    public SpellDef<? extends Spell> customSymbolPath(Identifier path){
        customTextureId= path;
        return this;
    }
    public SpellDef<? extends Spell> useTinted2dSymbol(Integer... tints){
        return useTinted2dSymbol(new Identifier(TheSpellLibrary.MOD_ID,"item/spell/tintable_simple"),tints);
    }
    public SpellDef<? extends Spell> useTinted2dSymbol(Identifier texturePath, Integer... tints){
        tintedTexture2d = texturePath;
        this.tints.addAll(List.of(tints));
        return this;
    }
    public SpellDef<? extends Spell> setCooldown(int cooldown){
        isSingleUse=false;
        this.cooldown= cooldown;
        return this;
    }
    public SpellDef<? extends Spell> setMana(float amount){
        this.manaCost=amount;
        return this;
    }
    public SpellDef<? extends Spell> setCastTime(int castTime){
        this.castTime =castTime;
        return this;
    }
    public SpellDef<? extends Spell> setRarity(Rarity rarity){
        this.rarity =rarity;
        return this;
    }
    public SpellDef<? extends Spell> setSound(SoundEvent castingSound, SoundEvent releasingSound){
        this.castingSound= castingSound;
        this.releasingSound= releasingSound;
        return this;
    }

    public Spell newSpell(World world, LivingEntity user){
        return template.createFromTemplate(this,world,user);
    }
    public Identifier getId(){
        return SpellRegistry.SPELL_TYPES.getId(this);
    }
    public Identifier getModelId() {
        Identifier spellId= SpellRegistry.SPELL_TYPES.getId(this);
        String path;
        String nameSpace;
        if (spellId==null){
            path="empty";
            nameSpace= TheSpellLibrary.MOD_ID;
        }
        else {
            path= spellId.getPath();
            nameSpace= spellId.getNamespace();
        }
        return Identifier.of(nameSpace,path);
    }
    public Identifier symbolTextureTinted2dBase(){
        return tintedTexture2d;
    }

    public ArrayList<Integer> getTints() {
        return tints;
    }

    /**
     * Returns id of this SpellType's texture or {{null}} if spell is empty or not registered.
     * This texture is located at mod_id/textures/item/spell/spell_id
     */
    @Nullable
    public Identifier symbolTexture() {
        if (customTextureId!=null){
            return customTextureId;
        }
        Identifier spellId= SpellRegistry.SPELL_TYPES.getId(this);
        String path;
        String nameSpace;
        if (spellId==null && isEmpty()){
            path="empty";
            nameSpace= TheSpellLibrary.MOD_ID;
        }
        else {
            path= spellId.getPath();
            nameSpace= spellId.getNamespace();
        }
        return Identifier.of(nameSpace,"item/spell/"+path);
    }
    /**
     * Returns id of the simplified texture of this SpellType or {{null}} if spell is empty or not registered.
     * This texture is located at mod_id/textures/item/spell/spell_id_simple
    */
    @Nullable
    public Identifier symbolTextureFor2d(){
        Identifier base = symbolTexture();
        if (base==null){
            return null;
        }
        return Identifier.of(base.getNamespace(),base.getPath()+"_simple");
    }
    public boolean usesTintedTexture(){
        return tintedTexture2d!=null && !tints.isEmpty();
    }
    public boolean isEmpty(){
        return this.getId() == SpellDefs.EMPTY.getId();
    }

    public int getCastTime() {
        return castTime;
    }
    public static  List<SpellDef<?>> getSpellsByRarity(Rarity rarity, List<SpellDef<?>> spellDefs) {
        return spellDefs.stream()
                .filter(spell -> spell.rarity == rarity)
                .toList();
    }
}
