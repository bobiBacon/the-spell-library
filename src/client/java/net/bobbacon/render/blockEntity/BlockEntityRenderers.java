package net.bobbacon.render.blockEntity;

import net.bobbacon.block.entity.ModBEs;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BlockEntityRenderers {
    public static void init(){
        BlockEntityRendererFactories.register(
                ModBEs.DECRYPTOR,
                DecryptorRenderer::new
        );
    }
}
