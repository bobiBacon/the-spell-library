package net.bobbacon.render.spell;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.render.RenderUtils;
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
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;

public class CircularSpellRenderer<T extends CircularAreaSpell> extends AreaSpellRenderer<T>{

    @Override
    public void renderCasting(WorldRenderContext context, T spell, PlayerEntity player,MatrixStack matrices) {


        matrices.push();


        renderCircle(matrices,context,spell.range,spell.type.school.color);
        super.renderCasting(context, spell, player, matrices);
        matrices.pop();
    }



    private void renderCircle(MatrixStack matrices, WorldRenderContext context,float radius, int color) {
        PlayerEntity player= MinecraftClient.getInstance().player;
        renderQuadOnVisible(Utils.getSphere(player.getBlockPos(),radius),matrices,color);
    }
}
