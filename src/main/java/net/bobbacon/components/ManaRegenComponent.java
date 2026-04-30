package net.bobbacon.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.bobbacon.spell.SpellSchool;
import net.minecraft.entity.player.PlayerEntity;

public class ManaRegenComponent extends SpellStatsBySchoolComponent{
    public ManaRegenComponent(PlayerEntity player) {
        super(player, 0.05f);
    }

    @Override
    public ComponentKey<? extends SpellStatsBySchoolComponent> getKey() {
        return ModComponents.MANA_REGEN;
    }


}
