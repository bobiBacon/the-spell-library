package net.bobbacon.render.blockEntity;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.TheSpellLibraryClient;
import net.bobbacon.block.entity.Decryptor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

import java.util.HashMap;
import java.util.Map;

public class DecryptorRenderer implements BlockEntityRenderer<Decryptor> {
    private final ItemRenderer itemRenderer;
    private final Map<Decryptor,DecryptionTableRenderer> renderers= new HashMap<>();
    protected int angle;

    public DecryptorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(
            Decryptor entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay
    ) {
        ItemStack stack = entity.getStack();

        if (stack.isEmpty()) return;
        light = WorldRenderer.getLightmapCoordinates(
                entity.getWorld(),
                entity.getPos().up()
        );
        DecryptionTableRenderer tableRenderer= renderers.get(entity);
        if (tableRenderer==null){
            tableRenderer= new DecryptionTableRenderer(this);
            renderers.put(entity,tableRenderer);
        }
        tableRenderer.renderTick(entity, tickDelta, matrices, vertexConsumers, light, overlay);
        matrices.push();

        matrices.translate(0.5, 1.2, 0.4);


        int angle2 = 0;
        if (entity.isInEndAnimation()){
            float progress= entity.endAnimationProgress();
            float coefficient= 0.4f/(progress+0.3f)-0.3f;
            matrices.translate(0,-0.425f*(1f-coefficient),0);
            angle2= 360-(int) (angle*(coefficient));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(67.5f-67.5f*coefficient));

        }else if (entity.isDecrypting()){
            angle= (int) ((MinecraftClient.getInstance().world.getTime() % 60) * 6);
            angle2=angle;
        }else if(entity.isIdle()){
            matrices.translate(0,-0.425f,0);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(67.5f));
        }

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle2));

        matrices.scale(0.65f, 0.65f, 0.65f);

        itemRenderer.renderItem(
                stack,
                ModelTransformationMode.FIXED,
                light,
                overlay,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                0
        );

        matrices.pop();
    }
}
