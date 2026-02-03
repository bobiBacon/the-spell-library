package net.bobbacon.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderUtil {
    public static void render2dItemLike(MatrixStack matrices, VertexConsumerProvider consumers, int red, int green, int blue, int overlay, Identifier texture){
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        MatrixStack.Entry entry = matrices.peek();
        Matrix3f normal = entry.getNormalMatrix();

        MinecraftClient client = MinecraftClient.getInstance();
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(texture);


        VertexConsumer vc = consumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
        );
        int light = 0xF000F0;

        vc.vertex(matrix, -0.5f, 0.5f, 0)
                .color(red, green, blue, 255)
                .texture(sprite.getMaxU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();
        vc.vertex(matrix, 0.5f, 0.5f, 0)
                .color(red, green, blue, 255)
                .texture(sprite.getMaxU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();
        vc.vertex(matrix, 0.5f, -0.5f, 0)
                .color(red, green, blue, 255)
                .texture(sprite.getMinU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();
        vc.vertex(matrix, -0.5f, -0.5f, 0)
                .color(red, green, blue, 255)
                .texture(sprite.getMinU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();
    }
}
