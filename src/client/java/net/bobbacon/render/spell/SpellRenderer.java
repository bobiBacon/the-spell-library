package net.bobbacon.render.spell;

import net.bobbacon.spell.Spell;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.entity.player.PlayerEntity;

public interface SpellRenderer {
    public  void renderCasting(WorldRenderContext context, Spell spell, PlayerEntity player);
    public void reset();


}
