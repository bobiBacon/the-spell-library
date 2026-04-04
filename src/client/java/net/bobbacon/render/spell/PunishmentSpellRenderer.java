package net.bobbacon.render.spell;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.TheSpellLibraryClient;
import net.bobbacon.spell.PunishmentSpell;
import net.bobbacon.spell.Spell;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class PunishmentSpellRenderer extends ConicalSpellRenderer implements SpellRenderer{
    protected Vec3d orientation=null;
    double angle= -1;


    @Override
    public void renderingTick(WorldRenderContext context, Spell spell, MatrixStack matrices) {
        PunishmentSpell punishmentSpell= (PunishmentSpell)spell;
        MinecraftClient client= MinecraftClient.getInstance();
        ItemRenderer itemRenderer= client.getItemRenderer();
        matrices.push();
        if (orientation==null){
            orientation= spell.orientation.multiply(1,0,1).normalize();
            angle= Math.acos(orientation.x)*Math.signum(orientation.z)+Math.PI;
            if (orientation.z==0&&orientation.x<0){
                angle=Math.PI;
            }
        }
        matrices.translate(spell.pos.getX()+orientation.x*3,spell.pos.getY()+3,spell.pos.getZ()+orientation.z*3);
        matrices.scale(4,4,4);
        float coefficient = Math.min((float) punishmentSpell.age / (punishmentSpell.getMaxTime()/3f),1);
        float angle2= (float) ((coefficient*coefficient-0.5)*Math.PI/2);

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotation((float) angle));

        matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) angle2),0.75f,-0.5f,0);
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        itemRenderer.renderItem(Items.GOLDEN_SWORD.getDefaultStack(), ModelTransformationMode.FIXED,255, OverlayTexture.DEFAULT_UV,matrices,immediate,client.world,0);
        immediate.draw();
        matrices.pop();
    }


}
