package net.bobbacon;

import net.bobbacon.attributes.ModEntityAttributes;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.block.entity.ModBEs;
import net.bobbacon.item.ModItems;
import net.bobbacon.ritual.RitualManager;
import net.bobbacon.sound.ModSounds;
import net.bobbacon.spell.SpellDefs;
import net.fabricmc.api.ModInitializer;

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
	}
}