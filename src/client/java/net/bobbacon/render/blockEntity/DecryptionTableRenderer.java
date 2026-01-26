package net.bobbacon.render.blockEntity;

import net.bobbacon.block.entity.Decryptor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class DecryptionTableRenderer {
//    public final BlockPos origin;
    public ArrayList<DecryptionTableParticleRenderer> particles= new ArrayList<>();
    public final DecryptorRenderer renderer;

    public DecryptionTableRenderer(DecryptorRenderer renderer) {
        this.renderer=renderer;

        Random random= new Random();
        for (int i = 1; i <= 8; i++) {
            for (int j = 0; j < 4; j++) {
                DecryptionTableParticleRenderer particle= new DecryptionTableParticleRenderer(random.nextFloat()+0.75f,random.nextInt(359),new Vec2f(
                        j*3f+i%2,i
                ), random.nextFloat()+1f);
               particles.add(particle);
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

        for (DecryptionTableParticleRenderer particleRenderer: particles){

            if (!entity.isIdle()){
                particleRenderer.turningTick(entity.endAnimationProgress());
            }
            if (entity.isInEndAnimation()){
                particleRenderer.aspiringTick(entity.endAnimationProgress());
            }
            particleRenderer.render(entity, tickDelta, matrices, vertexConsumers, light);
        }
    }
}
