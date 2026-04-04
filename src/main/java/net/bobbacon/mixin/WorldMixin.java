package net.bobbacon.mixin;

import net.bobbacon.Accessors.WorldAccessor;
import net.bobbacon.spell.SpellTickingManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(World.class)
public class WorldMixin implements WorldAccessor {
    @Unique
    public SpellTickingManager spellTickingManager= new SpellTickingManager();
    @Override
    public SpellTickingManager getSpellTickingManager() {
        return spellTickingManager;
    }
}
