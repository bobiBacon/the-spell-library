package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;

public class SpellDefs {
    private static final RegistryHelper<SpellDef<?>> registryHelper= new RegistryHelper<>(SpellRegistry.SPELL_TYPES, TheSpellLibrary.MOD_ID);

    public static final SpellDef<?> Example= registryHelper.register("example",new SpellDef<>(new ExampleSpell(),20).setCooldown(60).useTinted2dSymbol(0x50FF50).setRarity(Rarity.EPIC));
    public static final SpellDef<?> FireBall= registryHelper.register("fire_ball",new SpellDef<>(new ProjectileShootingSpell(EntityType.FIREBALL),20)
            .setCooldown(60).customSymbolPath(new Identifier("minecraft","item/fire_charge")).useTinted2dSymbol(0xFF3010).setSound(ModSounds.FIREBALL_CASTING,ModSounds.FIREBALL_RELEASING));
    public static final SpellDef<?> Sheeeeep= registryHelper.register("sheeeeep",new SpellDef<>(new ProjectileShootingSpell(EntityType.SHEEP),10).setCooldown(20).useTinted2dSymbol(0xFFEEEE).setRarity(Rarity.UNCOMMON));
    public static final SpellDef<?> EMPTY= registryHelper.register("empty",new SpellDef<>(new Spell(),0).notInDefaultLootTable());
    public static void init(){
    }
    public static List<SpellDef<?>> getAllDefaultLootTableSpells(){
        List<SpellDef<?>> spellDefs = new java.util.ArrayList<>(SpellRegistry.SPELL_TYPES.stream().toList());
        spellDefs.removeIf(spellDef -> !spellDef.usesDefaultLootTable);
        return spellDefs;
    }

}
