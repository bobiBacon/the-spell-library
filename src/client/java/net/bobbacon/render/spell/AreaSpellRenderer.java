package net.bobbacon.render.spell;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.render.RenderUtils;
import net.bobbacon.spell.AreaSpell;
import net.bobbacon.spell.CircularAreaSpell;
import net.bobbacon.spell.Spell;
import net.bobbacon.utils.Utils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;

public class AreaSpellRenderer implements SpellRenderer{
    private static HashMap<LivingEntity, Integer> map= new HashMap<>();

    @Override
    public void renderCasting(WorldRenderContext context, Spell spell, PlayerEntity player, MatrixStack matrices) {
        AreaSpell areaSpell= (AreaSpell) spell;

        HashMap<LivingEntity,Integer> copy= new HashMap<>();
        List<LivingEntity> list=areaSpell.targetEntities();
        list.removeIf(Entity::isInvisible);
        for (LivingEntity entity:list){
            copy.put(entity, map.getOrDefault(entity, 0));
            int value=copy.get(entity);
            float scale=1;
            if (value<=20){
                scale= (float) (1/(Math.abs(-(Math.pow((-value/1.5f+15f)/10f,4))+1f)+1f))+0.5f;
            }

            Vec3d entityPos= entity.getBoundingBox().getCenter();
            Vec3d playerPos= player.getBoundingBox().getCenter();
            Vec3d toPlayer= playerPos.subtract(entityPos);
            Vec3d symbolVec= toPlayer.multiply(1,0,1).normalize().multiply(entity.getWidth()/2);
            Vec3d symbolPos= symbolVec.add(entityPos);
            float halfWidth=entity.getWidth()/1.5f;
            Vec3d pos1= new Vec3d(halfWidth,halfWidth,0);
            Vec3d pos2= new Vec3d(-halfWidth,halfWidth,0);
            Vec3d pos3= new Vec3d(-halfWidth,-halfWidth,0);
            Vec3d pos4= new Vec3d(halfWidth,-halfWidth,0);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            matrices.push();
            float angle= MinecraftClient.getInstance().world.getTime()%360*2;
            double angle2= Math.acos(symbolVec.normalize().dotProduct(new Vec3d(0,0,1)));
            if (symbolVec.getX()<0){
                angle2=2*Math.PI-angle2;
            }
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) angle2), (float) symbolPos.getX(), (float) symbolPos.getY(), (float) symbolPos.getZ());
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle), (float) symbolPos.getX(), (float) symbolPos.getY(), (float) symbolPos.getZ());
            matrices.translate(symbolPos.getX(), symbolPos.getY(), symbolPos.getZ());
            matrices.scale(scale,scale,scale);
            Matrix4f transformationMatrix = matrices.peek().getPositionMatrix();


            long l = (MinecraftClient.getInstance().world.getTime() % 10) * 6;
            buffer.vertex(transformationMatrix, (float) pos1.getX(), (float) pos1.getY(), (float) pos1.getZ()).color(255,255,255,150).texture(1,1).next();
            buffer.vertex(transformationMatrix, (float) pos4.getX(), (float) pos4.getY(), (float) pos4.getZ()).color(255,255,255,150).texture(0,1).next();
            buffer.vertex(transformationMatrix,(float) pos3.getX(), (float) pos3.getY(), (float) pos3.getZ()).color(255,255,255,150).texture(0,0).next();
            buffer.vertex(transformationMatrix, (float) pos2.getX(), (float) pos2.getY(), (float) pos2.getZ()).color(255,255,255,150).texture(1,0).next();

            buffer.vertex(transformationMatrix, (float) pos1.getX(), (float) pos1.getY(), (float) pos1.getZ()).color(255,255,255,150).texture(1,1).next();
            buffer.vertex(transformationMatrix, (float) pos2.getX(), (float) pos2.getY(), (float) pos2.getZ()).color(255,255,255,150).texture(1,0).next();
            buffer.vertex(transformationMatrix,(float) pos3.getX(), (float) pos3.getY(), (float) pos3.getZ()).color(255,255,255,150).texture(0,0).next();
            buffer.vertex(transformationMatrix, (float) pos4.getX(), (float) pos4.getY(), (float) pos4.getZ()).color(255,255,255,150).texture(0,1).next();

            matrices.pop();
            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SrcFactor.SRC_ALPHA,
                    GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SrcFactor.ONE,
                    GlStateManager.DstFactor.ZERO
            );
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            RenderSystem.setShaderTexture(0, new Identifier(TheSpellLibrary.MOD_ID, "textures/misc/target.png"));

            tessellator.draw();

            RenderSystem.disableBlend();
            if (value<=20){
                copy.put(entity,value+1);
            }
        }
        map.clear();
        map=new HashMap<>(copy);
    }

    @Override
    public void renderingTick(WorldRenderContext context, Spell spell, MatrixStack matrices) {

    }

    @Override
    public void reset() {
        map.clear();
    }
    public void renderQuadOnVisible(List<BlockPos> posList,MatrixStack matrices,int color){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        for (BlockPos pos: posList){
            World world=MinecraftClient.getInstance().world;
            if (world.getBlockState(pos.down()).isSolid()&&!world.getBlockState(pos).isSolid()) {
                matrices.push();
                matrices.translate(pos.getX(), pos.getY()+0.001, pos.getZ());
                Matrix4f transformationMatrix = matrices.peek().getPositionMatrix();
                int red= RenderUtils.getRedFromHexa(color);
                int green= RenderUtils.getGreenFromHexa(color);
                int blue= RenderUtils.getBlueFromHexa(color);
                buffer.vertex(transformationMatrix, 0, 0, 0).color(red,green,blue,100).next();
                buffer.vertex(transformationMatrix, 0, 0, 1).color(red,green,blue,100).next();
                buffer.vertex(transformationMatrix, 1, 0, 1).color(red,green,blue,100).next();
                buffer.vertex(transformationMatrix, 1, 0, 0).color(red,green,blue,100).next();
                matrices.pop();
            }
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
    }
}
