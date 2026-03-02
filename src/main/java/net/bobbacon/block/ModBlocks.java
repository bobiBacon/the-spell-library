package net.bobbacon.block;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.block.entity.Decryptor;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.enums.Instrument;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {
    private static final RegistryHelper<Block> registryHelper=new RegistryHelper<>(Registries.BLOCK, TheSpellLibrary.MOD_ID);

    public static final Block DECRYPTOR = registryHelper.register("decryptor",new DecryptorBlock(FabricBlockSettings.create().sounds(BlockSoundGroup.STONE).instrument(Instrument.BASS).strength(2.5F).requiresTool()));

    public static void init(){

    }
}
