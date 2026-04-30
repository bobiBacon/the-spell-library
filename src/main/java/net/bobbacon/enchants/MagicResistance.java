package net.bobbacon.enchants;

import net.bobbacon.TheSpellLibraryDataGenerator;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;

public class MagicResistance extends ProtectionEnchantment {
    public MagicResistance(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, Type.FIRE, slotTypes);
    }

    @Override
    public int getProtectionAmount(int level, DamageSource source) {
        return source.isIn(TheSpellLibraryDataGenerator.ModTagGenerator.Magic)?level*2:0;
    }
}
