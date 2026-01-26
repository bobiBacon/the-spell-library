package net.bobbacon.block.entity;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBEs {
    public static final BlockEntityType<Decryptor> DECRYPTOR = register(
            "decryptor",
            BlockEntityType.Builder.create(Decryptor::new, ModBlocks.DECRYPTOR).build(null)
    );
    public static void init(){

    }
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(TheSpellLibrary.MOD_ID, path), blockEntityType);
    }
}
