package net.bobbacon.spell;

import net.bobbacon.TheSpellLibrary;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class SpellSchool {
    public final Style style;
    public final int color;
    public final int textureU;
    public final int textureV;
    public final Identifier texture;

    public SpellSchool(Style style, int color, int textureU, int textureV, Identifier texture) {
        this.style = style;
        this.color = color;
        this.textureU = textureU;
        this.textureV = textureV;
        this.texture = texture;
    }

    protected SpellSchool(Style style, int color, int textureV) {
        this(style,color,0,textureV,Identifier.of(TheSpellLibrary.MOD_ID,"textures/gui/mana_bar.png"));
    }
}
