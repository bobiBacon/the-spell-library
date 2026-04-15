package net.bobbacon.render.spell;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.spell.ConicalAreaSpell;
import net.bobbacon.spell.Spell;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class ConicalSpellRenderer<T extends ConicalAreaSpell> extends  AreaSpellRenderer<T>{
    @Override
    public void renderCasting(WorldRenderContext context, T spell, PlayerEntity player, MatrixStack matrices) {
        super.renderCasting(context, spell, player, matrices);
        renderQuadOnVisible(getPos(player,(ConicalAreaSpell) spell),matrices,spell.type.school.color);
//        Identifier animationId = new Identifier(TheSpellLibrary.MOD_ID, "spell_casting_test");
//        KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(animationId);
//        AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer((ClientPlayerEntity) player);
//        animationStack.addAnimLayer(1500,new KeyframeAnimationPlayer(animation));

    }
    public List<BlockPos> getPos(PlayerEntity player, ConicalAreaSpell spell){
        double r = spell.range;

        Vec3d origin = player.getCameraPosVec(1.0F);
        Vec3d look = player.getRotationVec(1.0F).normalize();

        double cosHalfAngle = Math.cos(Math.toRadians(spell.coneAngle / 2));

        Vec3d coneOrigin = origin.subtract(look);
        int minX = (int)Math.floor(player.getX() - r);
        int maxX = (int)Math.ceil(player.getX() + r);

        int minY = (int)Math.floor(player.getY() - r);
        int maxY = (int)Math.ceil(player.getY() + r);

        int minZ = (int)Math.floor(player.getZ() - r);
        int maxZ = (int)Math.ceil(player.getZ() + r);

        List<BlockPos> dots = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {

                    Vec3d p = new Vec3d(x + 0.5, y + 0.5, z + 0.5);



                    if (spell.isInArea(p, coneOrigin, origin, look, cosHalfAngle)) {
                        dots.add(new BlockPos((int) Math.floor(p.x), (int) Math.floor(p.y), (int) Math.floor(p.z)));
                    }
                }
            }
        }
        return dots;
    }
}
