package net.bobbacon.render.spell;

import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public interface SpellRendererFactory {
    SpellRenderer create();
}
