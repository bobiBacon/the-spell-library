package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpellType<T extends Spell> {
    private static final RegistryHelper<SpellType<?>> registryHelper= new RegistryHelper<>(SpellRegistry.SPELL_TYPES, TheSpellLibrary.MOD_ID);

    public static final SpellType<?> Example= registryHelper.register("example",new SpellType<>(ExampleSpell::new));
    public static final SpellType<?> EMPTY= registryHelper.register("empty",new SpellType<>(Spell::new));

    public boolean creativeTab= true;
    public static void init(){

    }

    private final SpellFactory<T> factory;


    public SpellType(SpellFactory<T> factory) {
        this.factory = factory;
    }
    public T create(World world, LivingEntity player){
        return factory.create(this,world,player);
    }

    public Identifier getId(){
        return SpellRegistry.SPELL_TYPES.getId(this);
    }
    public SpellType<T> hideInCreativeTab(){
        creativeTab=false;
        return this;
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
        return this.getId() == EMPTY.getId();
    }

    public interface SpellFactory<T extends Spell> {
        T create(SpellType<T> type, World world, LivingEntity player);
    }
}
