package net.bobbacon.render.gui;

import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.spell.SpellType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class CooldownRenderer {
    public static void renderCooldown(DrawContext context,SpellType<?> type, PlayerEntity player, int x, int y){
        float cooldown= ((LivingEntityAccessor)player).the_spell_library$getSpellCooldowns().getCooldownProgress(type,0f);
        if (cooldown > 0) {
            int h = MathHelper.ceil(16 * cooldown);
//
//            context.fill(
//                    x,
//                    y + 16 - h,
//                    x + 16,
//                    y + 16,
//                    0x80FFFFFF
//            );

            context.fill(RenderLayer.getGuiOverlay(), x,
                    y + 16 - h,
                    x + 16,
                    y + 16, Integer.MAX_VALUE);
        }
    }
}
