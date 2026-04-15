package net.bobbacon.animations;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ComplexAnimations {
    private static final RegistryKey<Registry<ComplexAnimation.Factory>> ANIMATION_FACTORIES_KEY =
            RegistryKey.ofRegistry(new Identifier(TheSpellLibrary.MOD_ID, "complex_anim_factory"));
    public static final SimpleRegistry<ComplexAnimation.Factory> ComplexAnimationFactories = FabricRegistryBuilder.createSimple(ANIMATION_FACTORIES_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    private static final RegistryHelper<ComplexAnimation.Factory> registryHelper=new RegistryHelper<>(ComplexAnimationFactories,TheSpellLibrary.MOD_ID);
    public static final ComplexAnimation.Factory InWalkAnimation= registryHelper.register("in_walk",InWalkComplexAnimation::new);
    public static void init(){

    }
}
