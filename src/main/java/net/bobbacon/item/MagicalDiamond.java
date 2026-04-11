package net.bobbacon.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStackSet;

public class MagicalDiamond extends Item {
    public MagicalDiamond(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.isAlive()){
            attacker.setStackInHand(attacker.getActiveHand(),new ItemStack(ModItems.CryingDiamond,stack.getCount()));
        }
        return super.postHit(stack, target, attacker);
    }
}
