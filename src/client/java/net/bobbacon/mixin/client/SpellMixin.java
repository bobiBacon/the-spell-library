package net.bobbacon.mixin.client;

import net.bobbacon.accessor.ClientWorldAccessor;
import net.bobbacon.render.spell.SpellRenderer;
import net.bobbacon.render.spell.SpellRendererManager;
import net.bobbacon.render.spell.SpellRenderers;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellTickingManager;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.bobbacon.spell.Spell.class)
public class SpellMixin {
    @Inject(method = "stopCasting", at= @At("HEAD"),remap = false)
    private void injectAbort(CallbackInfo c){
//        Spell self= (Spell)(Object)this;
//        if (self.type.hasRenderer){
//            SpellRendererManager spellRendererManager=((ClientWorldAccessor) MinecraftClient.getInstance().world).getSpellRendererManager();
//            spellRendererManager.remove(self);
//        }
    }
}
