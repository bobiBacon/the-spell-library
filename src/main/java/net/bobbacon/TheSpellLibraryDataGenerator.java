package net.bobbacon;

import net.bobbacon.block.ModBlocks;
import net.bobbacon.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TheSpellLibraryDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelGenerator::new);
		pack.addProvider(ModTagGenerator::new);
		pack.addProvider(ModRecipeGenerator::new);
		pack.addProvider(ModLootGenerator::new);
	}
	public static class ModTagGenerator extends FabricTagProvider<DamageType> {
		public static final TagKey<DamageType> Magic = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(TheSpellLibrary.MOD_ID, "magic"));

		public ModTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(Magic)
					.add(DamageTypes.MAGIC)
					.add(DamageTypes.INDIRECT_MAGIC)
					.add(DamageTypes.DRAGON_BREATH)
					.add(DamageTypes.WITHER)
					.add(DamageTypes.LIGHTNING_BOLT);
		}

	}
	public static class ModModelGenerator extends FabricModelProvider {
		public ModModelGenerator(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			itemModelGenerator.register(ModItems.MagicalDiamond, Models.GENERATED);
			itemModelGenerator.register(ModItems.CryingDiamond, Models.GENERATED);
		}
	}
	public static class ModRecipeGenerator extends FabricRecipeProvider {

		public ModRecipeGenerator(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generate(Consumer<RecipeJsonProvider> consumer) {
			ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.DECRYPTOR).pattern("idi").pattern(" s ").pattern("sss")
					.input('i', Items.IRON_INGOT).input('d',Items.DIAMOND).input('s',Items.STONE_BRICKS)
					.criterion(FabricRecipeProvider.hasItem(ModItems.SCROLL),
							FabricRecipeProvider.conditionsFromItem(ModItems.SCROLL))
					.offerTo(consumer);
			ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC,ModItems.MagicalDiamond)
					.input(Items.DIAMOND).input(Items.LAPIS_LAZULI,2)
					.criterion(FabricRecipeProvider.hasItem(Items.DIAMOND),
							FabricRecipeProvider.conditionsFromItem(Items.DIAMOND))
					.offerTo(consumer);
		}
	}
	public static class ModLootGenerator extends FabricBlockLootTableProvider {


		protected ModLootGenerator(FabricDataOutput dataOutput) {
			super(dataOutput);
		}


		@Override
		public void generate() {
			addDrop(ModBlocks.DECRYPTOR);
		}


	}
}
