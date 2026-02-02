package net.bobbacon.mixin.client;

import net.bobbacon.item.ModItems;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.render.gui.CooldownRenderer;
import net.bobbacon.spell.SpellType;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V" ,shift = At.Shift.AFTER))
    private void drawItemInject(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci){
        if (!stack.isOf(ModItems.SCROLL)){
            return;
        }
        SpellType<?> type= ScrollItem.getSpell(stack);
        DrawContext self= ((DrawContext)(Object)this);
        CooldownRenderer.renderCooldown(self,type,self.client.player,x,y);
    }
}
