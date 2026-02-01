package net.bobbacon.mixin.client;

import net.bobbacon.Accessors.PlayerAccessor;
import net.bobbacon.TheSpellLibrary;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    private static final Identifier MANA_BAR = Identifier.of(TheSpellLibrary.MOD_ID,"textures/gui/mana_bar.png");

    @Inject(method = "renderStatusBars", at= @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
    private void renderCustomBars(DrawContext context, CallbackInfo ci){
        InGameHud self= (InGameHud) (Object)this;
        self.client.getProfiler().push("mana");
        PlayerAccessor playerEntity = (PlayerAccessor) self.getCameraPlayer();
        this.renderManaBar(context,playerEntity.the_spell_library$getMaxMana(),playerEntity.the_spell_library$getMana());

    }
    @Unique
    public  void renderManaBar(DrawContext context, float max, float amount){
//        TheSpellLibrary.LOGGER.info("render mana bar");
        InGameHud self= (InGameHud) (Object)this;

        self.client.getProfiler().push("manaBar");
        int x=self.scaledWidth/2 + 10;
        int y= self.scaledHeight - 45;

        PlayerEntity player= self.getCameraPlayer();
        int maxAir = player.getMaxAir();
        if (player.isSubmergedIn(FluidTags.WATER) || Math.min(player.getAir(), maxAir) < maxAir) {
            y-=10;
        }
        context.drawTexture(MANA_BAR, x,y, 0, 5, 81, 5);
        context.drawTexture(MANA_BAR, x ,y , 0, 0, (int) (81*amount/max), 5);

//        if (i > 0) {
//            int j = 182;
//            int k = (int)(this.client.player.experienceProgress * 183.0F);
//            int l = this.scaledHeight - 32 + 3;
//            if (k > 0) {
//                context.drawTexture(MANA_BAR, x, l, 0, 69, k, 5);
//            }
//        }

        self.client.getProfiler().pop();
    }

}
