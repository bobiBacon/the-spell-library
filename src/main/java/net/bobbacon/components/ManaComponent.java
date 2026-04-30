package net.bobbacon.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.bobbacon.spell.Mana;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellSchool;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.UUID;

public class ManaComponent implements AutoSyncedComponent {

    private final PlayerEntity player;
    private HashMap<SpellSchool, Mana> schoolsMana;

    public ManaComponent(PlayerEntity player) {
        this.player = player;
        schoolsMana= Mana.getEmptyMap(player);
    }

    public HashMap<SpellSchool, Mana> getSchoolsMana() {
        return schoolsMana;
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        schoolsMana.clear();

        NbtCompound manaTag = tag.getCompound("SchoolsMana");

        for (String key : manaTag.getKeys()) {
            SpellSchool school = SpellRegistry.SPELL_SCHOOL.get(Identifier.tryParse(key));
            NbtCompound schoolTag = manaTag.getCompound(key);

            Mana mana = new Mana(player);
            mana.readFromNbt(schoolTag);

            schoolsMana.put(school, mana);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound manaTag = new NbtCompound();

        for (var entry : schoolsMana.entrySet()) {
            NbtCompound schoolTag = new NbtCompound();
            Mana mana = entry.getValue();

            mana.writeToNbt(schoolTag);



            manaTag.put(String.valueOf(SpellRegistry.SPELL_SCHOOL.getId(entry.getKey())), schoolTag);
        }

        tag.put("SchoolsMana", manaTag);
    }
}
