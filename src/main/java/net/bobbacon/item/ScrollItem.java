package net.bobbacon.item;

import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellDefs;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ScrollItem extends Item {
    private static final String SPELL_KEY = "spell";
    private static final String DECRYPTED_KEY = "decrypted";
    private static final String PLAYERS_KEY = "players";
    public ScrollItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack= user.getStackInHand(hand);
        Spell spell= getSpell(stack).newSpell(world,user);
        boolean b= spell.canCast(user.getBlockPos());
        if (b){
            user.setCurrentHand(hand);
            spell.playCastingSound(user.getBlockPos());
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        Spell spell= getSpell(stack).newSpell(world,user);
        boolean b= spell.tryCast(user.getBlockPos());
        if (!b){
            return super.finishUsing(stack, world, user);
        }
        boolean b2= true;
        if (user instanceof PlayerEntity player){
            b2= !player.isCreative();


        }
        if (spell.isSingleUse()){
            if (b2){
                stack.decrement(1);
            }
        }
        //apply cooldown
        ((LivingEntityAccessor)user).the_spell_library$getSpellCooldowns()

                .set(spell.type, spell.cooldownTime());

        return stack;
    }



    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        Spell spell= getSpell(stack).newSpell(world,user);
        spell.castingTick(user.getBlockPos(),remainingUseTicks);
        if (remainingUseTicks==0){
            finishUsing(stack,world,user);
            return;
        }
        if (((getMaxUseTime(stack)-remainingUseTicks)&11111)==0){
//            Spell spell= getSpell(stack).newSpell(world,user);
//            spell.playCastingSound(user.getBlockPos());
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    public static SpellDef<?> getSpell(ItemStack stack){
        if (!stack.hasNbt()) return SpellDefs.EMPTY;

        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains(SPELL_KEY, NbtElement.STRING_TYPE)) return SpellDefs.EMPTY;

        Identifier id = Identifier.tryParse(nbt.getString(SPELL_KEY));
        if (id == null) return  SpellDefs.EMPTY;
        SpellDef<?> spellDef=SpellRegistry.SPELL_TYPES.get(id);
        return spellDef!=null?spellDef:SpellDefs.EMPTY;
    }
    public static void setSpell(ItemStack stack, SpellDef<?> spell) {
        Identifier id= SpellRegistry.SPELL_TYPES.getId(spell);
        if (id==null){
            stack.getOrCreateNbt().putString(SPELL_KEY, "empty");
        }else {
            stack.getOrCreateNbt().putString(SPELL_KEY, id.toString());
        }
    }
    public static boolean canRead(PlayerEntity player, ItemStack stack){
        if (player.isCreative()||getSpell(stack).isEmpty()){
            return true;
        }
        for (NbtElement nbt:stack.getOrCreateNbt().getList(PLAYERS_KEY,NbtElement.STRING_TYPE)){
            NbtString nbt2= (NbtString) nbt;
            UUID uuid= UUID.fromString(nbt2.asString());
            if (player.getUuid().equals(uuid)){
                return true;
            }
        }
        return false;
    }


    public static void decrypt(PlayerEntity player,ItemStack stack){
        NbtCompound nbt= stack.getOrCreateNbt();
        NbtList list= nbt.getList(PLAYERS_KEY,NbtElement.STRING_TYPE);
        list.add(NbtString.of(player.getUuid().toString()));
        nbt.put(PLAYERS_KEY,list);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        SpellDef<?> spell = getSpell(stack);
        if (spell.isEmpty()){
             return "item.the-spell-library.scroll.blank";
         }
        return super.getTranslationKey(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        SpellDef<?> spell= getSpell(stack);
        return spell.getCastTime();
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return getSpell(stack).rarity;
    }


}
