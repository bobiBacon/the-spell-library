package net.bobbacon.sound;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final RegistryHelper<SoundEvent> registryHelper= new RegistryHelper<>(Registries.SOUND_EVENT, TheSpellLibrary.MOD_ID);
    public static final SoundEvent DEFAULT_CASTING= register("spell.casting.default");
    public static final SoundEvent DEFAULT_RELEASING= register("spell.releasing.default");
    public static final SoundEvent FIREBALL_CASTING= register("spell.casting.fireball");
    public static final SoundEvent FIREBALL_RELEASING= register("spell.releasing.fireball");
    public static final SoundEvent DECRYPTOR= register("block.decryptor");

    public static SoundEvent register(String name){
        Identifier id= Identifier.of(TheSpellLibrary.MOD_ID,name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static void init(){

    }
}
