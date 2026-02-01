package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpellType<T extends Spell> {


    public boolean creativeTab= true;
    public boolean isSingleUse= true;
    public int cooldown= 0;

    private final SpellFactory<T> factory;


    public SpellType(SpellFactory<T> factory) {
        this.factory = factory;
    }
    public SpellType<T> hideInCreativeTab(){
        creativeTab=false;
        return this;
    }
    public SpellType<T> cooldown(int cooldown){
        isSingleUse=false;
        this.cooldown= cooldown;
        return this;
    }

    public T create(World world, LivingEntity user){
        return factory.create(this,world,user);
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
    /**
     * Returns id of this SpellType's texture or {{null}} if spell is empty or not registered.
     * This texture is located at mod_id/textures/item/spell/spell_id
     */
    @Nullable
    public Identifier symbolTexture() {
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
    public boolean isEmpty(){
        return this.getId() == SpellTypes.EMPTY.getId();
    }

    public interface SpellFactory<T extends Spell> {
        T create(SpellType<T> type, World world, LivingEntity player);
    }
}
