package net.bobbacon.mixin.client;

import net.bobbacon.Accessors.WorldAccessor;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.accessor.ClientWorldAccessor;
import net.bobbacon.render.spell.SpellRendererManager;
import net.bobbacon.ritual.RitualManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements ClientWorldAccessor {
    @Unique
    private final SpellRendererManager spellRendererManager= new SpellRendererManager();
    @Inject(method = "tick", at = @At("HEAD"))
    private void onWorldTick(CallbackInfo ci) {
        ClientWorld world = (ClientWorld) (Object) this;
        ((WorldAccessor)world).getSpellTickingManager().tickAll();
    }


    @Override
    public SpellRendererManager getSpellRendererManager() {
        return spellRendererManager;
    }
}
