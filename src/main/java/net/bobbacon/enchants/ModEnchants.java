package net.bobbacon.enchants;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;

public class ModEnchants {
    private static final RegistryHelper<Enchantment> registryHelper= new RegistryHelper<>(Registries.ENCHANTMENT, TheSpellLibrary.MOD_ID);
    public static final Enchantment MAGIC_RESISTANCE = registryHelper.register("magic_resistance",new MagicResistance(Enchantment.Rarity.UNCOMMON, EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET));
    public static void init(){

    }
}
