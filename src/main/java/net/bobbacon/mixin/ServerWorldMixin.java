package net.bobbacon.mixin;

import net.bobbacon.Accessors.WorldAccessor;
import net.bobbacon.ritual.RitualManager;
import net.bobbacon.spell.SpellTickingManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements WorldAccessor {
    @Shadow public abstract boolean spawnNewEntityAndPassengers(Entity entity);

    @Unique
    public SpellTickingManager spellTickingManager= new SpellTickingManager();
    @Inject(method = "tick", at = @At("HEAD"))
    private void onWorldTick(CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        RitualManager.get(world).tick();
        spellTickingManager.tickAll();
    }

    @Override
    public SpellTickingManager getSpellTickingManager() {
        return spellTickingManager;
    }
}
