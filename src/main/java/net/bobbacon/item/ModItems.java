package net.bobbacon.item;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;

public class ModItems {
    private static final RegistryHelper<Item> registryHelper=new RegistryHelper<>(Registries.ITEM, TheSpellLibrary.MOD_ID);
    public static final Item SCROLL = registryHelper.register("scroll", new ScrollItem(new Item.Settings()));


    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(entries -> {
                    for (SpellType<?> spell : SpellRegistry.SPELL_TYPES) {
                        if (!spell.isEmpty()&&spell.creativeTab){
                            ItemStack stack = new ItemStack(ModItems.SCROLL);
                            ScrollItem.setSpell(stack, spell);
                            entries.add(stack);
                        }else {
                            entries.add(ModItems.SCROLL.getDefaultStack());
                        }
                    }
                });
    }
}
