package net.bobbacon.mixin;

import net.bobbacon.Accessors.EntityAccessor;
import net.bobbacon.ritual.RitualManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onDeath",at = @At("TAIL"))
    private void onDeathInject(DamageSource damageSource, CallbackInfo ci){
        LivingEntity self= (LivingEntity) (Object) this;
        if (!self.getWorld().isClient&&((EntityAccessor)this).night_of_the_Dead$comesFromRitual()){
            RitualManager.get((ServerWorld) self.getWorld()).onEntityDeath((LivingEntity) (Object) this);
        }
    }
}
