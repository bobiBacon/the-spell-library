package net.bobbacon.render.blockEntity;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.TheSpellLibraryClient;
import net.bobbacon.block.DecryptorBlock;
import net.bobbacon.block.entity.Decryptor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.RotationAxis;

import java.util.HashMap;
import java.util.Map;

public class DecryptorRenderer implements BlockEntityRenderer<Decryptor> {
    private final ItemRenderer itemRenderer;
    private final Map<Decryptor,DecryptionTableRenderer> renderers= new HashMap<>();
    protected int angle;
//    static {
//        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((BlockEntity entity, ClientWorld world)->{
//            if (entity instanceof Decryptor decryptor){
//                DecryptionTableRenderer renderer= renderers.
//            }
//        });
//    }
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

        if (stack.isEmpty()) {
            renderers.remove(entity);
            return;
        }
        light = WorldRenderer.getLightmapCoordinates(
                entity.getWorld(),
                entity.getPos().up()
        );
        matrices.push();
        BlockState state= entity.getWorld().getBlockState(entity.getPos());
        int rotation = switch (state.get(Properties.HORIZONTAL_FACING)){
            case WEST -> 270;
            case EAST -> 90;
            case SOUTH -> 180;
            default -> 0;
        };
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(rotation),0.5f,0.5f,0.5f);

        matrices.push();

        matrices.translate(0.5, 1.2, 0.4);


        int angle2 = 0;
        if (entity.isInEndAnimation()){
            float progress= entity.endAnimationProgress();
            float coefficient= 0.4f/(progress+0.306226f)-0.306226f;
            matrices.translate(0,-0.425f*(1f-coefficient),0);
            angle2= (int) (angle*(coefficient));
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

        DecryptionTableRenderer tableRenderer= renderers.get(entity);
        if (tableRenderer==null){
            tableRenderer= new DecryptionTableRenderer(this,entity.endAnimationProgress());
            renderers.put(entity,tableRenderer);
        }
        tableRenderer.renderTick(entity, tickDelta, matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }
}
