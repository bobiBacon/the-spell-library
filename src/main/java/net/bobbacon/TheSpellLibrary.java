package net.bobbacon;

import net.bobbacon.attributes.ModEntityAttributes;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.block.entity.ModBEs;
import net.bobbacon.item.ModItems;
import net.bobbacon.loot.*;
import net.bobbacon.particles.ModParticles;
import net.bobbacon.ritual.RitualManager;
import net.bobbacon.sound.ModSounds;
import net.bobbacon.spell.SpellDefs;
import net.bobbacon.spell.SpellSchool;
import net.bobbacon.spell.SpellSchools;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TheSpellLibrary implements ModInitializer {
	public static final String MOD_ID = "the-spell-library";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		SpellDefs.init();
		ModItems.init();
		RitualManager.init();
		ModBlocks.init();
		ModBEs.init();
		ModEntityAttributes.init();
		ModSounds.init();
		ModLoot.init();
		Predicates.init();
		ModParticles.init();
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (id.equals(new Identifier("minecraft", "entities/witch"))) {

				addSpellLoot(tableBuilder,1f/30f,new ModifierBuilder().add(2,SpellSchools.Arcanic));
			}
			if (id.equals(new Identifier("minecraft", "entities/evoker"))) {
				addSpellLoot(tableBuilder,1f/30f,new ModifierBuilder().add(0.3f,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("minecraft", "chests/desert_pyramid"))) {
				addSpellLoot(tableBuilder,1/10f,new ModifierBuilder().add(1.5f,SpellSchools.Fire).add(0,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("minecraft", "chests/jungle_temple"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(1.5f,SpellSchools.Wind).add(0.1f,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("minecraft", "chests/stronghold_library"))) {
				addSpellLoot(tableBuilder,1f,new ModifierBuilder().add(false,SpellSchools.Fire,SpellSchools.Wind));
			}
			if (id.equals(new Identifier("minecraft", "chests/stronghold_corridor"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(0.3f,SpellSchools.Necromancy).add(0f,SpellSchools.Fire));
			}
			if (id.equals(new Identifier("minecraft", "chests/end_city_treasure"))) {
				addSpellLoot(tableBuilder,1f/5f,new ModifierBuilder().add(0.3f,SpellSchools.Fire).add(1.5f,SpellSchools.Arcanic));
			}
			if (id.equals(new Identifier("minecraft", "chests/simple_dungeon"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(false,SpellSchools.Wind).add(0.4f,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("minecraft", "chests/abandoned_mineshaft"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(0.4f,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("minecraft", "chests/ancient_city"))) {
				addSpellLoot(tableBuilder,1f/5f,new ModifierBuilder().add(false,SpellSchools.Fire,SpellSchools.Divine).add(1.5f,SpellSchools.Necromancy));
			}
			if (id.getPath().contains("bastion")) {
				addSpellLoot(tableBuilder,1f/8f,new ModifierBuilder().add(3f,SpellSchools.Fire));
			}
			if (id.equals(new Identifier("minecraft", "chests/igloo_chest"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(0.4f,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("minecraft", "chests/buried_treasure"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(0.4f,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("minecraft", "chests/nether_bridge"))) {
				addSpellLoot(tableBuilder,1f/8f,new ModifierBuilder().add(3f,SpellSchools.Fire));
			}
			if (id.equals(new Identifier("minecraft", "chests/pillager_outpost"))) {
				addSpellLoot(tableBuilder,1f/8f,new ModifierBuilder().add(false,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("minecraft", "chests/ruined_portal"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(true,SpellSchools.Fire));
			}
			if (id.equals(new Identifier("minecraft", "chests/shipwreck_treasure"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(false,SpellSchools.Fire,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("betternether", "chests/city"))) {
				addSpellLoot(tableBuilder,1f/8f,new ModifierBuilder().add(true,SpellSchools.Fire));
			}
			if (id.equals(new Identifier("betternether", "chests/city_common"))) {
				addSpellLoot(tableBuilder,1f/10f,new ModifierBuilder().add(true,SpellSchools.Fire));
			}
			if (id.equals(new Identifier("betternether", "chests/ghast_hive"))) {
				addSpellLoot(tableBuilder,1/5f,new ModifierBuilder().add(true,SpellSchools.Fire));
			}
			if (id.equals(new Identifier("betternether", "chests/library"))) {
				addSpellLoot(tableBuilder,1/2f,new ModifierBuilder().add(1.5f,SpellSchools.Fire));
			}
			if (id.equals(new Identifier("betternether", "chests/wither_tower"))) {
				addSpellLoot(tableBuilder,1/5f,new ModifierBuilder().add(1.5f,SpellSchools.Fire).add(1.5f,SpellSchools.Necromancy));
			}
			if (id.equals(new Identifier("betternether", "chests/wither_tower_bonus"))) {
				addSpellLoot(tableBuilder,1/3f,new ModifierBuilder().add(1.5f,SpellSchools.Fire).add(1.5f,SpellSchools.Necromancy));
			}



		});
	}

	private static void addSpellLoot(LootTable.Builder tableBuilder,float chance, ModifierBuilder builder) {
		LootPool.Builder poolBuilder = null;
		try {

			poolBuilder = LootPool.builder()
					.rolls(ConstantLootNumberProvider.create(1))
					.conditionally(RandomChanceLootCondition.builder(chance))
					.with(ItemEntry.builder(ModItems.SCROLL))
					.apply(RandomSpellLootFunction.builder(Predicates.AlwaysTrueLoot, builder.build()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		tableBuilder.pool(poolBuilder);
	}
	public class ModifierBuilder {
		private final ArrayList<SpellLootModifier> modifiers;
		public ModifierBuilder() {
            this.modifiers = new ArrayList<>();
        }

		public ModifierBuilder add(float i, SpellSchool school){
			modifiers.add(new BySchoolModifier(i,school));
			return this;
		}
		public ModifierBuilder add(boolean includeOrExclude,SpellSchool... school){
			if (includeOrExclude){
				modifiers.add(new OnlySchools(List.of(school)));
			}
			else {
				modifiers.add(new ExcludeSchools(List.of(school)));
			}
			return this;
		}
		public ArrayList<SpellLootModifier> build(){
			return new ArrayList<>(modifiers);
		}
	}
}