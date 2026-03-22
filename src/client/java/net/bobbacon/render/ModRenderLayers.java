package net.bobbacon.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class ModRenderLayers {
    public static void init(){

    }

    public static RenderLayer magic(Identifier tex) {
        return RenderLayer.of(
                "magic_layer",
                VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                256,
                false,
                true,
                RenderLayer.MultiPhaseParameters.builder()

                        // Shader normal translucent
                        .program(RenderPhase.ENTITY_TRANSLUCENT_CULL_PROGRAM)

                        // Texture
                        .texture(new RenderPhase.Texture(tex, false, false))

                        // Alpha blending
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)

                        // Depth test normal
                        .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)

                        // ❗ Empêche d'écrire dans le Z-buffer
                        .writeMaskState(RenderPhase.COLOR_MASK)

                        // Pas de cull
                        .cull(RenderPhase.DISABLE_CULLING)

                        // Lumière active
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)

                        .build(false)
        );

    }
    public static RenderLayer magicCircle(Identifier tex) {
        return RenderLayer.of(
                "magic_circle",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                256,
                true,
                true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.ENTITY_TRANSLUCENT_PROGRAM)
                        .texture(new RenderPhase.Texture(tex, false, false))
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                        .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                        .writeMaskState(RenderPhase.ALL_MASK)
                        .build(false)
        );

    }
}
