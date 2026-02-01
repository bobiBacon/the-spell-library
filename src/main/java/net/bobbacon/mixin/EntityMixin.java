package net.bobbacon.mixin;

import net.bobbacon.Accessors.EntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
public class EntityMixin implements EntityAccessor {
    @Unique
    private static final String comesFromRitualKey= "comes_from_ritual";
    @Unique
    private static final String RitualKey= "ritual";
    @Unique
    public boolean comesFromRitual= false;
    @Unique
    public UUID ritualId=null;
    @Inject(method = "writeNbt", at=@At("TAIL"))
    private void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir){
        nbt.putBoolean(comesFromRitualKey,comesFromRitual);
        if (comesFromRitual){
            nbt.putUuid(RitualKey,ritualId);
        }
    }
    @Inject(method = "readNbt", at=@At("TAIL"))
    private void readNbt(NbtCompound nbt, CallbackInfo ci){
        comesFromRitual=nbt.getBoolean(comesFromRitualKey);
        if (comesFromRitual){
            ritualId= nbt.getUuid(RitualKey);
        }
    }

    @Override
    public boolean the_spell_library$comesFromRitual() {
        return comesFromRitual;
    }

    @Override
    public void the_spell_library$setComesFromRitual(boolean value, UUID id) {
        comesFromRitual= value;
        ritualId= id;
    }
}