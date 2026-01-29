package net.bobbacon.render.blockEntity;

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
    public final float sizeX;
    public final float sizeY;


    public DecryptionTableParticleRenderer(float y, int angle, Vec2f origin, float speed, float sizeX, float sizeY, float endProgress) {
        this.y = y;
        this.x=origin.x;
        this.z=origin.y;
        this.originalY= y;
        this.angle= angle;
        this.origin = new Vec3d(origin.x*0.0625+0.1875,0.80, origin.y*0.0625-0.2f);
        this.speed=speed;
        posOnTable= origin;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        aspiringTick(endProgress);
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
    public void renderSecond(
            Decryptor entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ){
        MinecraftClient client = MinecraftClient.getInstance();
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(Identifier.of(TheSpellLibraryClient.MOD_ID,"block/decryption_tablet"));

        VertexConsumer consumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucentCull(sprite.getAtlasId())

        );


        matrices.push();

        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(22.5f),0,1.5f,0);
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90),x,y,z);
        matrices.translate(x-0.03125,y,z);
        matrices.scale(0.125f,0.125f,0.125f);


        light=255;

        render(entity,matrices,sprite,consumer,light);

        matrices.pop();
    }
    public void renderMain(
            Decryptor entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ){
        MinecraftClient client = MinecraftClient.getInstance();
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(Identifier.of(TheSpellLibraryClient.MOD_ID,"block/decryption_tablet"));

        VertexConsumer consumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucentCull(sprite.getAtlasId())
        );


        matrices.push();
        int angle2= 90+angle;

        float progress= entity.endAnimationProgress();
        float coefficient= 0.4f/(progress+0.306226f)-0.306226f;
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


        light=255;

        render(entity,matrices,sprite,consumer,light);

        matrices.pop();
    }
    public void render(
            Decryptor entity,
            MatrixStack matrices,
            Sprite sprite,
            VertexConsumer consumer,
            int light
    ){

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();

        int overlay = OverlayTexture.DEFAULT_UV;
        float u0 = sprite.getMinU();
        float u1 = sprite.getMaxU();
        float v0 = sprite.getMinV();
        float v1 = sprite.getMaxV();

        float du = (u1 - u0) / 16f;
        float dv = (v1 - v0) / 16f;


        float minU= sprite.getMinU()+du* posOnTable.x;
        float maxU= minU+du*sizeX;
        float minV= sprite.getMinV()+dv* posOnTable.y;
        float maxV= minV+dv*sizeY;
        consumer.vertex(matrix, -0.25f*sizeX, -0.25f*sizeY,0)
                .color(255, 255, 255, 150)
                .texture(minU, maxV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.25f*sizeX, -0.25f*sizeY,0 )
                .color(255, 255, 255, 150)
                .texture(maxU, maxV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.25f*sizeX, 0.25f*sizeY,0)
                .color(255, 255, 255, 150)
                .texture(maxU, minV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, -0.25f*sizeX, 0.25f*sizeY,0)
                .color(255, 255, 255, 150)
                .texture(minU, minV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();
        // BACK (ordre inversé)
        consumer.vertex(matrix, -0.25f*sizeX, 0.25f*sizeY, 0)
                .color(255,255,255,150)
                .texture(minU, minV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, -1)
                .next();

        consumer.vertex(matrix, 0.25f*sizeX, 0.25f*sizeY, 0)
                .color(255,255,255,150)
                .texture(maxU, minV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, -1)
                .next();

        consumer.vertex(matrix, 0.25f*sizeX, -0.25f*sizeY, 0)
                .color(255,255,255,150)
                .texture(maxU, maxV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, -1)
                .next();

        consumer.vertex(matrix, -0.25f*sizeX, -0.25f*sizeY, 0)
                .color(255,255,255,150)
                .texture(minU, maxV)
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, -1)
                .next();

    }

}

