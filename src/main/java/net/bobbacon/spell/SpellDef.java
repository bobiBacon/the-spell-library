package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

//defines properties of a spell
public class  SpellDef<T extends Spell> {


    public boolean creativeTab= true;
    public boolean isSingleUse= true;
    public Identifier customTextureId= null;
    public int cooldown= 0;
    public float manaCost=0;
    public int primaryColor=0;
    public int secondaryColor=0;
    public Identifier tintedTexture2dId = null;
    private final Spell template;

    public SpellDef(Spell template, float manaCost) {
        this.manaCost= manaCost;
        this.template = template;
    }
    public SpellDef<? extends Spell> hideInCreativeTab(){
        creativeTab=false;
        return this;
    }
    public SpellDef<? extends Spell> customSymbolPath(Identifier path){
        customTextureId= path;
        return this;
    }
    public SpellDef<? extends Spell> useTinted2dSymbol(int primaryColor, int secondaryColor){
        return useTinted2dSymbol(primaryColor,secondaryColor,new Identifier(TheSpellLibrary.MOD_ID,"item/spell/tintable_simple"));
    }
    public SpellDef<? extends Spell> useTinted2dSymbol(int primaryColor, int secondaryColor,Identifier texturePath){
        this.primaryColor= primaryColor;
        this.secondaryColor= secondaryColor;
        this.tintedTexture2dId = texturePath;
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

    public Spell newSpell(World world, LivingEntity user){
        return template.createFromTemplate(this,world,user);
    }
    public void cast(BlockPos pos){

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
        return new Identifier(tintedTexture2dId.getNamespace(), tintedTexture2dId.getPath()+"_base");
    }
    public Identifier symbolTextureTinted2dOverlay(){
        return new Identifier(tintedTexture2dId.getNamespace(), tintedTexture2dId.getPath()+"_overlay");
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
        return primaryColor!=0 && secondaryColor!=0;
    }
    public boolean isEmpty(){
        return this.getId() == SpellDefs.EMPTY.getId();
    }

}
