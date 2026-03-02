package net.bobacon;

import net.bobbacon.block.ModBlocks;
import net.bobbacon.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import java.util.function.Consumer;

public class TheSpellLibraryDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelGenerator::new);
		pack.addProvider(ModRecipeGenerator::new);
		pack.addProvider(ModLootGenerator::new);
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
