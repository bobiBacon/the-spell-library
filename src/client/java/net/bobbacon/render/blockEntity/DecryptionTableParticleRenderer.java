package net.bobbacon.render.blockEntity;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.TheSpellLibraryClient;
import net.bobbacon.block.entity.Decryptor;
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
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DecryptionTableParticleRenderer {
    public float x;
    public float y;
    public final float originalY;
    public float z;
    protected int angle;
    public final Vec3d origin;
    public final Vec2f posOnTable;
    public final float speed;


    public DecryptionTableParticleRenderer(float y, int angle, Vec2f origin, float speed) {
        this.y = y;
        this.originalY= y;
        this.angle= angle;
        this.origin = new Vec3d(origin.x*0.0625+0.1875,0.80, origin.y*0.0625-0.2f);
        this.speed=speed;
        posOnTable= origin;
    }
    public void turningTick(float endProgress){

        if (endProgress>0){
            if(endProgress>=0.85&&(angle>354||angle<6)){
                angle=0;
                updatePosAfterRotation();
                return;
            }
            angle += (int) (10f *  (1f-endProgress));
        }else {
            angle+=10;
        }
        if (angle>=360){
            angle= angle%360;
        }
        updatePosAfterRotation();

    }
    protected void updatePosAfterRotation(){
        double rad= Math.toRadians(angle);
        x= (float) Math.cos(rad)+0.5f;
        z=(float) Math.sin(rad)+0.5f;
        y=originalY;
    }
    public void aspiringTick(float progress){
        Vec3d pos1= new Vec3d(x,y,z);
        Vec3d pos2= pos1.lerp(origin,progress);
        x= (float) pos2.x;
        y= (float) pos2.y;
        z= (float) pos2.z;

    }
    public void render(
            Decryptor entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ){
        if (entity.isIdle()){
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(Identifier.of(TheSpellLibraryClient.MOD_ID,"block/decryption_tablet"));

        VertexConsumer consumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucent(sprite.getAtlasId())
        );

        matrices.push();
        int angle2= 90+angle;

        float progress= entity.endAnimationProgress();
        float coefficient= 0.4f/(progress+0.3f)-0.3f;
        if (progress>=0.5){
            if (angle<=180){
                angle2= (int) (angle2*coefficient);
            }else {
                angle2= (int) (360-angle2*coefficient);
            }
        }
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(22.5f*(1f-coefficient)),0,1.5f,0);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(angle2),x,y,z);
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90*(1f-coefficient)),x,y,z);
        matrices.translate(x,y,z);
        matrices.scale(0.125f,0.125f,0.125f);

//        matrices.scale(0.125f,0.0625f,0.125f);
//        matrices.translate(0.5,0.5,0.5);
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();

        int overlay = OverlayTexture.DEFAULT_UV;
        light=255;
        float u0 = sprite.getMinU();
        float u1 = sprite.getMaxU();
        float v0 = sprite.getMinV();
        float v1 = sprite.getMaxV();

        float du = (u1 - u0) / 16f;
        float dv = (v1 - v0) / 16f;

//        consumer.vertex(matrix, -0.5f, -0.5f,0)
//                .color(255, 255, 255, 255)
//                .texture(sprite.getMinU(), sprite.getMaxV())
//                .overlay(overlay)
//                .light(light)
//                .normal(normal, 0, 0, 1)
//                .next();
//
//        consumer.vertex(matrix, 0.5f, -0.5f,0 )
//                .color(255, 255, 255, 255)
//                .texture(sprite.getMaxU(), sprite.getMaxV())
//                .overlay(overlay)
//                .light(light)
//                .normal(normal, 0, 0, 1)
//                .next();
//
//        consumer.vertex(matrix, 0.5f, 0.5f,0)
//                .color(255, 255, 255, 255)
//                .texture(sprite.getMaxU(), sprite.getMinV())
//                .overlay(overlay)
//                .light(light)
//                .normal(normal, 0, 0, 1)
//                .next();
//
//        consumer.vertex(matrix, -0.5f, 0.5f,0)
//                .color(255, 255, 255, 255)
//                .texture(sprite.getMinU(), sprite.getMinV())
//                .overlay(overlay)
//                .light(light)
//                .normal(normal, 0, 0, 1)
//                .next();
        float minU= sprite.getMinU()+du* posOnTable.x;
        float maxU= minU+du*2;
        float minV= sprite.getMinV()+du* posOnTable.y;
        float maxV= minV+du;

        consumer.vertex(matrix, -0.5f, -0.25f,0)
                .color(255, 255, 255, 150)
                .texture(minU, maxV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, -0.25f,0 )
                .color(255, 255, 255, 150)
                .texture(maxU, maxV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, 0.25f,0)
                .color(255, 255, 255, 150)
                .texture(maxU, minV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, -0.5f, 0.25f,0)
                .color(255, 255, 255, 150)
                .texture(minU, minV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        matrices.pop();
    }
}
