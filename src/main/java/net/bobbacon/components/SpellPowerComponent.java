package net.bobbacon.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.entity.player.PlayerEntity;

public class SpellPowerComponent extends SpellStatsBySchoolComponent{
    public SpellPowerComponent(PlayerEntity player) {
        super(player, 1);
    }

    @Override
    public ComponentKey<? extends SpellStatsBySchoolComponent> getKey() {
        return ModComponents.SPELL_POWER;
    }
}
