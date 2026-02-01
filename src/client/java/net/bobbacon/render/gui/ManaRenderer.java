package net.bobbacon.render.gui;

import net.bobbacon.TheSpellLibrary;
import net.minecraft.client.gui.DrawContext;

//public class ManaRenderer {
//    public static void renderManaBar(DrawContext context, float max, float amount){
//        TheSpellLibrary.LOGGER.info("render mana bar");
//        this.client.getProfiler().push("expBar");
//        int i = this.client.player.getNextLevelExperience();
//        if (i > 0) {
//            int j = 182;
//            int k = (int)(this.client.player.experienceProgress * 183.0F);
//            int l = this.scaledHeight - 32 + 3;
//            context.drawTexture(ICONS, x, l, 0, 64, 182, 5);
//            if (k > 0) {
//                context.drawTexture(ICONS, x, l, 0, 69, k, 5);
//            }
//        }
//
//        this.client.getProfiler().pop();
//    }
//}
