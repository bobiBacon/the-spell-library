package net.bobbacon.render.spell;

import net.bobbacon.spell.Spell;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public interface   SpellRenderer<T extends Spell> {
    public  void renderCasting(WorldRenderContext context, T spell, PlayerEntity player, MatrixStack matrices);
    public  void renderingTick(WorldRenderContext context, T spell, MatrixStack matrices);
    public void reset();


}
