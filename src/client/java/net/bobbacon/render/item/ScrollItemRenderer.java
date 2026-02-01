package net.bobbacon.render.item;

import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.SpellType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ScrollItemRenderer {

    public static void renderSpellSymbolGui(
            ItemStack stack,
            MatrixStack matrices,
            VertexConsumerProvider consumers,
            int overlay
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        SpellType<?> spell = ScrollItem.getSpell(stack);
        if (spell == null || spell.isEmpty()) return;
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(spell.symbolTextureFor2d());


        matrices.push();

        // On se place AU-DESSUS de l'item
        matrices.translate(-0.1f, -0.1f, 1);
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90));

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        VertexConsumer vc = consumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
        );

        int light = 0xF000F0; // FULL BRIGHT

        float min = 4f;
        float max = 12f;
        MatrixStack.Entry entry = matrices.peek();
        Matrix3f normal = entry.getNormalMatrix();

        vc.vertex(matrix, -0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        vc.vertex(matrix, 0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        vc.vertex(matrix, 0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        vc.vertex(matrix, -0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        matrices.pop();
    }

    public static void renderSymbol(
            ItemStack stack,
            MatrixStack matrices,
            VertexConsumerProvider vertices,
            int light,
            boolean leftHanded,
            ModelTransformationMode renderMode,
            BakedModel scrollModel
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        SpellType<?> spell = ScrollItem.getSpell(stack);
        if (spell == null || spell.isEmpty()) return;


        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(spell.symbolTexture());

        VertexConsumer consumer = vertices.getBuffer(
                RenderLayer.getEntityCutoutNoCull(sprite.getAtlasId())
        );

        matrices.push();



        matrices.scale(0.3f, 0.3f, 0.3f);
        float x=-0.45f;
        if (leftHanded){
            x=(-1)*x;
        }

        matrices.multiply(
                RotationAxis.POSITIVE_Y.rotationDegrees(
                        (MinecraftClient.getInstance().world.getTime() % 90) * 4
                ),
                x,0.9f,0.6f
        );
        matrices.translate(x, 0.9, 0.6);

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();

        int overlay = OverlayTexture.DEFAULT_UV;
        light=255;

        consumer.vertex(matrix, -0.5f, -0.5f,0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, -0.5f,0 )
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, 0.5f,0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, -0.5f, 0.5f,0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        matrices.pop();
    }
}
