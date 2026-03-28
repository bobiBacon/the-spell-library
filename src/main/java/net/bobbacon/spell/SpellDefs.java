package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.particles.ModParticles;
import net.bobbacon.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;

public class SpellDefs {
    private static final RegistryHelper<SpellDef<?>> registryHelper= new RegistryHelper<>(SpellRegistry.SPELL_TYPES, TheSpellLibrary.MOD_ID);

    public static final SpellDef<?> Example= Registry.register(SpellRegistry.SPELL_TYPES,new Identifier(TheSpellLibrary.MOD_ID,"example"),new SpellDef<>(new ExampleSpell(),SpellSchools.Arcanic,20).setCooldown(60).useTinted2dSymbol(0x50FF50).setRarity(Rarity.EPIC));
    public static final SpellDef<?> FireBall= registryHelper.register("fire_ball",new SpellDef<>(new ProjectileShootingSpell(EntityType.FIREBALL),SpellSchools.FIRE,20)
            .setCooldown(60).customSymbolPath(new Identifier("minecraft","item/fire_charge")).useTinted2dSymbol(new Identifier(TheSpellLibrary.MOD_ID,"item/spell/tintable_fire"),0xcc3300,0xe6b800)).setSound(ModSounds.FIREBALL_CASTING,ModSounds.FIREBALL_RELEASING);
    public static final SpellDef<?> FlameBlast= registryHelper.register("flame_blast",new SpellDef<>(new InstantDamageSpell(20,5, DamageTypes.IN_FIRE, ParticleTypes.FLAME),SpellSchools.FIRE,10)
            .setCooldown(20).customSymbolPath(new Identifier("minecraft","item/blaze_powder")).useTinted2dSymbol(new Identifier(TheSpellLibrary.MOD_ID,"item/spell/tintable_fire"),0xcc3300,0xe6b800).setSound(ModSounds.FIREBALL_CASTING,ModSounds.FIREBALL_RELEASING));
    public static final SpellDef<?> FireWave= registryHelper.register("fire_wave",new SpellDef<>(new FireWaveSpell(8,70),SpellSchools.FIRE,40)
            .setCooldown(80).customSymbolPath(new Identifier("minecraft","item/blaze_powder")).useTinted2dSymbol(new Identifier(TheSpellLibrary.MOD_ID,"item/spell/tintable_fire"),0xFF8000,0xFFFF66).setSound(ModSounds.FIREBALL_CASTING,ModSounds.FIREBALL_RELEASING).withRenderer());
    public static final SpellDef<?> Blindness= registryHelper.register("blindness",new SpellDef<>(new EffectTargetingSpell(20,ParticleTypes.SQUID_INK,new StatusEffectInstance(StatusEffects.BLINDNESS,300,0)),SpellSchools.Necromancy,40)
            .setCooldown(1200).useTinted2dSymbol(0x330080).setCastTime(50));
    public static final SpellDef<?> BigFireBall= registryHelper.register("big_fire_ball",new SpellDef<>(new BigFireBallSpell(),SpellSchools.FIRE,60)
            .customSymbolPath(new Identifier("minecraft","item/fire_charge")).useTinted2dSymbol(new Identifier(TheSpellLibrary.MOD_ID,"item/spell/tintable_fire"),0xcc3300,0xe6b800).setSound(ModSounds.FIREBALL_CASTING,ModSounds.FIREBALL_RELEASING).setCastTime(40).setRarity(Rarity.RARE));
    public static final SpellDef<?> Sheeeeep= registryHelper.register("sheeeeep",new SpellDef<>(new ProjectileShootingSpell(EntityType.SHEEP),SpellSchools.Conjuration,10).setCooldown(20).useTinted2dSymbol(0xFFEEEE).setRarity(Rarity.UNCOMMON));
    public static final SpellDef<?> WindDash= registryHelper.register("wind_dash",new SpellDef<>(new DashSpell(3),SpellSchools.Wind,10).setCooldown(40).useTinted2dSymbol(0xFFDDFF).setCastTime(10).setRarity(Rarity.COMMON));
    public static final SpellDef<?> WindBurst= registryHelper.register("wind_burst",new SpellDef<>(new ProjectionSpell(10,ParticleTypes.ELECTRIC_SPARK,3,ModParticles.WIND_PROJECTION_PARTICLE),SpellSchools.Wind,10).setCooldown(40).useTinted2dSymbol(0xFFDDFF).setCastTime(10).setRarity(Rarity.COMMON));
    public static final SpellDef<?> FireBoom= registryHelper.register("fire_boom",new SpellDef<>(new FireBoom(10,ParticleTypes.FLAME,2,ModParticles.FIRE_PARTICLE),SpellSchools.FIRE,20).setCooldown(200).useTinted2dSymbol(new Identifier(TheSpellLibrary.MOD_ID,"item/spell/tintable_fire"),0xcc3300,0xe6b800).setSound(ModSounds.FIREBALL_CASTING,ModSounds.FIREBALL_RELEASING).setCastTime(10).setRarity(Rarity.COMMON));
    public static final SpellDef<?> Gust= registryHelper.register("gust",new SpellDef<>(new LargeProjectionSpell(8,70,3),SpellSchools.Wind,30).setCooldown(200).useTinted2dSymbol(0xFFDDFF).setCastTime(30).withRenderer());
    public static final SpellDef<?> DarkAura= registryHelper.register("dark_aura",new SpellDef<>(new DarkAura(8),SpellSchools.Necromancy,6).setCooldown(200).useTinted2dSymbol(0x222222).setCastTime(60).withRenderer().setRarity(Rarity.UNCOMMON));

    public static final SpellDef<?> EMPTY= registryHelper.register("empty",new SpellDef<>(new Spell(),0).notInDefaultLootTable());
    public static void init(){
    }
    public static List<SpellDef<?>> getAllDefaultLootTableSpells(){
        List<SpellDef<?>> spellDefs = new java.util.ArrayList<>(SpellRegistry.SPELL_TYPES.stream().toList());
        spellDefs.removeIf(spellDef -> !spellDef.usesDefaultLootTable);
        return spellDefs;
    }

}
