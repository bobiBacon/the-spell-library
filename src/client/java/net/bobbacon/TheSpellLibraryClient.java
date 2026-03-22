package net.bobbacon;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.particles.ModParticlesClient;
import net.bobbacon.render.ModRenderLayers;
import net.bobbacon.render.blockEntity.BlockEntityRenderers;
import net.bobbacon.spell.SpellDef;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class TheSpellLibraryClient implements ClientModInitializer {
    public static final String MOD_ID = "the-spell-library";

    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BlockEntityRenderers.init();
        ModRenderLayers.init();
        ModParticlesClient.init();
        ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
            if (!(stack.getItem() instanceof ScrollItem)) return;

            SpellDef<?> spell = ScrollItem.getSpell(stack);
            if (spell == null||spell.isEmpty()) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            //TODO descriptions
            if (ScrollItem.canRead(client.player,stack)){
                tooltip.addAll(spell.newSpell(client.world, client.player).getTooltips());
            } else {
            }
        });
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            MatrixStack matrices = context.matrixStack();
            Camera camera = context.camera();

            Vec3d camPos = camera.getPos();

            matrices.push();
            matrices.translate(0, -1.6, 0);
//            matrices.translate(-camPos.x, -camPos.y, -camPos.z);

            renderCircle(matrices,context);

            matrices.pop();
        });
	}
    private void renderCircle(MatrixStack matrices, WorldRenderContext context) {
        Matrix4f transformationMatrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        float radius = 8.0f;
        int segments = 20;

        buffer.vertex(transformationMatrix, 0, 0, 0).color(170,30,30,100).next();


        for (float i = 0; i <= segments; i++) {
            double angle2 = 2f * Math.PI * -i/segments;
            float x2 = (float)(Math.cos(angle2) * radius);
            float z2 = (float)(Math.sin(angle2) * radius);
            buffer.vertex(transformationMatrix, x2, 0, z2).color(150,20,20,100).next();

        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.SRC_ALPHA,
                GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SrcFactor.ONE,
                GlStateManager.DstFactor.ZERO
        );

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);

        tessellator.draw();

        RenderSystem.disableBlend();
//        Matrix4f matrix = matrices.peek().getPositionMatrix();
//
//
//
//        float radius = 8.0f;
//        int segments = 10;
//
//        float y = 0.01f; // légèrement au-dessus du sol pour éviter le z-fighting
//
//        VertexConsumerProvider consumers = context.consumers();
//
//        for (float i = 0; i < segments; i++) {
//            VertexConsumer buffer = consumers
//                    .getBuffer(ModRenderLayers.magicCircle(new Identifier("minecraft","textures/misc/white.png")));
//            double angle1 = 2f * Math.PI * i / (float)segments;
//            double angle2 = 2f * Math.PI * (i + 1f) / (float)segments;
//
//            float x1 = (float)(Math.cos(angle1) * radius);
//            float z1 = (float)(Math.sin(angle1) * radius);
//
//            float x2 = (float)(Math.cos(angle2) * radius);
//            float z2 = (float)(Math.sin(angle2) * radius);
//
//            // triangle centre -> p1 -> p2
//            buffer.vertex(matrix, x1/radius*1f, y, z1/radius*1f).color(0, 0, 255, 100).normal(matrices.peek().getNormalMatrix(), 0, 1, 0)
//                    .overlay(OverlayTexture.DEFAULT_UV)
//                    .light(15728880)
//                    .texture(0f,0f).next();
//            buffer.vertex(matrix, x1, y, z1).color(0, 0, 255, 100).normal(matrices.peek().getNormalMatrix(), 0, 1, 0)
//                    .overlay(OverlayTexture.DEFAULT_UV)
//                    .light(15728880)
//                    .texture(0,1).next();
//            buffer.vertex(matrix, x2, y, z2).color(0, 0, 255, 100).normal(matrices.peek().getNormalMatrix(), 0, 1, 0)
//                    .overlay(OverlayTexture.DEFAULT_UV)
//                    .light(15728880)
//                    .texture(1,1).next();
//            buffer.vertex(matrix, x2/radius*1f, y, z2/radius*1f).color(0, 0, 255, 100).normal(matrices.peek().getNormalMatrix(), 0, 1, 0)
//                    .overlay(OverlayTexture.DEFAULT_UV)
//                    .light(15728880)
//                    .texture(1,0).next();
//        }
    }
}