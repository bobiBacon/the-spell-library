package net.bobbacon.render.blockEntity;

import net.bobbacon.TheSpellLibraryClient;
import net.bobbacon.block.entity.Decryptor;
import net.bobbacon.item.ScrollItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Random;

public class DecryptionTableRenderer {
//    public final BlockPos origin;
    public ArrayList<DecryptionTableParticleRenderer> particles= new ArrayList<>();
    public ArrayList<DecryptionTableParticleRenderer> secondaryParticles= new ArrayList<>();
    public final DecryptorRenderer renderer;
    public boolean empty= true;

    public DecryptionTableRenderer(DecryptorRenderer renderer,float endProgress) {
        this.renderer=renderer;

        Random random= new Random();
        for (int i = 1; i <= 8; i++) {
            for (int j = 0; j < 4; j++) {
                DecryptionTableParticleRenderer particle= new DecryptionTableParticleRenderer(random.nextFloat()+0.75f,random.nextInt(359),new Vec2f(
                        j*3f+i%2,i
                ), random.nextFloat()+1f,2,1, endProgress);
               particles.add(particle);
            }

        }
        for (int i = 1; i <= 8; i++) {
            for (int j = 0; j < 4; j++) {
                DecryptionTableParticleRenderer particle= new DecryptionTableParticleRenderer(0.75f,0,new Vec2f(
                        j*3f-(2*(i%2)-2),i
                ), 0,1,1, endProgress);
                secondaryParticles.add(particle);
            }

        }
    }
    public void renderTick(
            Decryptor entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay
    ){
        empty=false;
        if (entity.isDecrypted()){
            renderSymbol(ScrollItem.getSpell(entity.getStack()).symbolTexture(),tickDelta,matrices,vertexConsumers,light);
        }
        for (DecryptionTableParticleRenderer particleRenderer: particles){

            if (!entity.isDecrypted()){
                particleRenderer.turningTick(entity.endAnimationProgress());
            }else {
                particleRenderer.decryptedTick();
            }
            if (entity.isInEndAnimation()){
                particleRenderer.aspiringTick(entity.endAnimationProgress());
            }
            particleRenderer.renderMain(entity, tickDelta, matrices, vertexConsumers, light);
        }
        for (DecryptionTableParticleRenderer particleRenderer: secondaryParticles){

            float progress= entity.endAnimationProgress();
            if (progress>0.9f){
                if (entity.isDecrypted()){
                    particleRenderer.decryptedTick();
                }else {
                    particleRenderer.aspiringTick((progress-0.9f)*10f);
                }
                particleRenderer.renderSecond(entity, tickDelta, matrices, vertexConsumers, light);
            }
        }
    }
    protected void renderSymbol(
            Identifier textureId,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light){
        MinecraftClient client = MinecraftClient.getInstance();
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply((textureId));

        VertexConsumer consumer = vertexConsumers.getBuffer(
                RenderLayer.getEntitySmoothCutout(sprite.getAtlasId())
        );


        matrices.push();

        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(22.5f),0,1.5f,0);
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90),0.5f,0.801f,0.05f);
        matrices.translate(0.5,0.801,0.05);
        matrices.scale(0.4f,0.4f,0.4f);

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();

        float minU= sprite.getMinU();
        float maxU= sprite.getMaxU();
        float minV= sprite.getMinV();
        float maxV= sprite.getMaxV();
        int overlay = OverlayTexture.DEFAULT_UV;


        consumer.vertex(matrix, -0.5f, -0.5f,0)
                .color(255, 255, 255, 255)
                .texture(maxU, minV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, -0.5f,0 )
                .color(255, 255, 255, 255)
                .texture(minU, minV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, 0.5f,0)
                .color(255, 255, 255, 255)
                .texture(minU, maxV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, -0.5f, 0.5f,0)
                .color(255, 255, 255, 255)
                .texture(maxU, maxV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();
        matrices.pop();
    }
}
