package net.bobbacon.mixin;

import net.bobbacon.Accessors.EntityAccessor;
import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.ritual.RitualManager;
import net.bobbacon.spell.SpellCooldownManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityAccessor {
    @Inject(method = "onDeath",at = @At("TAIL"))
    private void onDeathInject(DamageSource damageSource, CallbackInfo ci){
        LivingEntity self= (LivingEntity) (Object) this;
        if (!self.getWorld().isClient&&((EntityAccessor)this).the_spell_library$comesFromRitual()){
            RitualManager.get((ServerWorld) self.getWorld()).onEntityDeath((LivingEntity) (Object) this);
        }
    }
    @Unique
    private final SpellCooldownManager spellCooldownManager= new SpellCooldownManager();
    @Override
    public SpellCooldownManager the_spell_library$getSpellCooldowns() {
        return spellCooldownManager;
    }
    @Inject(method = "tick", at= @At("TAIL"))
    private void spellTick(CallbackInfo ci){
        the_spell_library$getSpellCooldowns().update();
    }
}
