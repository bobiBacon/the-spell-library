package net.bobbacon.item;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellDef;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;

public class ModItems {
    private static final RegistryHelper<Item> registryHelper=new RegistryHelper<>(Registries.ITEM, TheSpellLibrary.MOD_ID);
    public static final Item SCROLL = registryHelper.register("scroll", new ScrollItem(new Item.Settings()));
    public static final Item DECRYPTOR = registryHelper.register("decryptor", new BlockItem(ModBlocks.DECRYPTOR, new Item.Settings()));
    public static final Item MagicalDiamond = registryHelper.register("magical_diamond", new MagicalDiamond(new Item.Settings()));
    public static final Item CryingDiamond = registryHelper.register("crying_diamond", new Item(new Item.Settings()));


    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(entries -> {
                    for (SpellDef<?> spell : SpellRegistry.SPELL_TYPES) {
                        if (!spell.isEmpty()&&spell.creativeTab){
                            ItemStack stack = new ItemStack(ModItems.SCROLL);
                            ScrollItem.setSpell(stack, spell);
                            entries.add(stack);
                        }else {
                            entries.add(ModItems.SCROLL.getDefaultStack());
                        }
                    }
                });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register(entries -> {
                    entries.add(DECRYPTOR);
                });
    }
}
