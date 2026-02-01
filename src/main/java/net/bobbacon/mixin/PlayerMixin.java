package net.bobbacon.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bobbacon.Accessors.PlayerAccessor;
import net.bobbacon.attributes.ModEntityAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMixin implements PlayerAccessor {
    @Shadow private ItemStack selectedItem;
    @Unique
    private static final TrackedData<Float> MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
    @Unique
    private static final TrackedData<Float> MAX_MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Override
    public float the_spell_library$getMana() {
        return ((PlayerEntity) (Object) this).getDataTracker().get(MANA);
    }

    @Override
    public float the_spell_library$getMaxMana() {
        return ((PlayerEntity) (Object) this).getDataTracker().get(MAX_MANA);
    }

    @Override
    public float the_spell_library$getManaRegenRate() {
        PlayerEntity self= ((PlayerEntity) (Object) this);
        return (float)self.getAttributeValue(ModEntityAttributes.GENERIC_MANA_REGEN);
    }


    @Override
    public void the_spell_library$setMaxMana(float amount) {
        ((PlayerEntity) (Object) this).getDataTracker().set(MAX_MANA, MathHelper.clamp(amount, 0.0F, this.the_spell_library$getMaxMana()));
        if (the_spell_library$getMana()>amount){
            the_spell_library$setMana(amount);
        }
    }

    @Override
    public void the_spell_library$setMana(float amount) {
        ((PlayerEntity) (Object) this).getDataTracker().set(MANA, MathHelper.clamp(amount, 0.0F, this.the_spell_library$getMaxMana()));
    }

    @Override
    public void the_spell_library$incrementMana(float amount) {
        float current= this.the_spell_library$getMana();
        this.the_spell_library$setMana(current+amount);
    }

    @Override
    public void the_spell_library$incrementMana() {
        this.the_spell_library$incrementMana(1);
    }

    @Override
    public void the_spell_library$decrementMana(float amount) {
        this.the_spell_library$incrementMana(-amount);
    }

    @Override
    public void the_spell_library$decrementMana() {
        this.the_spell_library$decrementMana(1);
    }

    @Inject(method = "initDataTracker", at= @At("TAIL"))
    public void initCustomTrackers(CallbackInfo ci){
        PlayerEntity self= ((PlayerEntity) (Object) this);
        self.getDataTracker().startTracking(MANA, 80.0F);
        self.getDataTracker().startTracking(MAX_MANA, 80.0F);
    }
    @Inject(method = "tick",at = @At("TAIL"))
    public void injectTick(CallbackInfo ci){
        PlayerEntity self= ((PlayerEntity) (Object) this);
        float maxHealth= Math.min(self.getMaxHealth(),20f);
        float minimumHealth= maxHealth*0.1f;
        if (maxHealth!=0&&self.getHealth()>minimumHealth){
            float i=  (self.getHealth()-minimumHealth)/(maxHealth-minimumHealth);
            the_spell_library$incrementMana(this.the_spell_library$getManaRegenRate()*i*i);
        }
    }
    @ModifyReturnValue(method = "createPlayerAttributes", at = @At("TAIL"))
    private static DefaultAttributeContainer.Builder customAttributes(DefaultAttributeContainer.Builder original){
        return original.add(ModEntityAttributes.GENERIC_MANA_REGEN,0.05);
    }
}
