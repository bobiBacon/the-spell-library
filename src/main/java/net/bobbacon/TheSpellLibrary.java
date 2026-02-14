package net.bobbacon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.bobbacon.attributes.ModEntityAttributes;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.block.entity.ModBEs;
import net.bobbacon.item.ModItems;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.loot.ModLoot;
import net.bobbacon.loot.Predicates;
import net.bobbacon.loot.RandomSpellLootFunction;
import net.bobbacon.loot.SetSpellFunction;
import net.bobbacon.ritual.RitualManager;
import net.bobbacon.sound.ModSounds;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellDefs;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtString;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (id.equals(new Identifier("minecraft", "entities/villager"))) {
				LOGGER.info("modify loot 2");

                LootPool.Builder poolBuilder = null;
                try {
                    poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(1f))
                            .with(ItemEntry.builder(ModItems.SCROLL))
                            .apply(RandomSpellLootFunction.builder(Predicates.isClericLoot));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

//				for (SpellDef<?> spellDef: SpellDefs.getAllDefaultLootTableSpells()){
//					ItemStack scroll= ModItems.SCROLL.getDefaultStack();
//					ScrollItem.setSpell(scroll,spellDef);
//					poolBuilder= poolBuilder.with(SetSpellFunction.builder(spellDef));
//				}


				tableBuilder.pool(poolBuilder);
			}
		});
	}
}