package net.bobbacon.render.item;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.render.LayeredTintedTexture;
import net.bobbacon.render.RenderUtils;
import net.bobbacon.spell.SpellDef;
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
import net.minecraft.util.Identifier;
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
        SpellDef<?> spell = ScrollItem.getSpell(stack);
        if (spell == null || spell.isEmpty()) return;
        Identifier texture;
        int red=255;
        int green= 255;
        int blue= 255;
        matrices.push();

        matrices.translate(0f, 0f, 1);
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90),0,0,1);
        if (spell.usesTintedTexture()){
            LayeredTintedTexture layeredTintedTexture= constructTexture(spell);
            for (int i = 0; i < layeredTintedTexture.length(); i++) {
                RenderUtils.render2dItemLike(matrices,consumers,layeredTintedTexture.getRed(i),layeredTintedTexture.getGreen(i),layeredTintedTexture.getBlue(i),overlay,layeredTintedTexture.getLayerPath(i));
            }
        }else {
            texture= spell.symbolTextureFor2d();
            RenderUtils.render2dItemLike(matrices,consumers,red,green,blue,overlay,texture);
        }






        matrices.pop();
    }
    protected static LayeredTintedTexture constructTexture(SpellDef<?> spellDef){
        return new LayeredTintedTexture(spellDef.symbolTextureTinted2dBase(),spellDef.getTints());
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
        SpellDef<?> spell = ScrollItem.getSpell(stack);
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
