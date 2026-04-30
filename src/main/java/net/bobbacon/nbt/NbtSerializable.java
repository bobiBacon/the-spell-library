package net.bobbacon.nbt;

import net.bobbacon.components.FloatWithModifiers;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellSchool;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.UUID;

public interface NbtSerializable {
    public void readFromNbt(NbtCompound tag);

    public void writeToNbt(NbtCompound tag);
}
