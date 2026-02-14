package net.bobbacon.loot;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.spell.SpellDef;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;

import java.util.function.Predicate;

public class Predicates {
    private static final RegistryKey<Registry<Predicate<?>>> PREDICATE_REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(TheSpellLibrary.MOD_ID, "predicate"));
    public static final SimpleRegistry<Predicate<?>> PREDICATES = FabricRegistryBuilder.createSimple(PREDICATE_REGISTRY_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    public static final Predicate<LootContext> isClericLoot= Registry.register(PREDICATES,Identifier.of(TheSpellLibrary.MOD_ID,"is_cleric_loot"),(lootContext -> {
        Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);

        if (!(entity instanceof VillagerEntity villager)) {
            return false;
        }
        return villager.getVillagerData().getProfession() == VillagerProfession.CLERIC;

    }));
    public static void init(){

    }
}
