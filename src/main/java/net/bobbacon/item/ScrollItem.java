package net.bobbacon.item;

import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellType;
import net.bobbacon.spell.SpellTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

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
        Spell spell= getSpell(stack).create(world,user);
        boolean b= spell.canCast(user.getBlockPos());
        if (b){
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        Spell spell= getSpell(stack).create(world,user);
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
        Spell spell= getSpell(stack).create(world,user);
        if (!spell.canCast(user.getBlockPos())){

        }
        if (remainingUseTicks==0){
            finishUsing(stack,world,user);
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    public static SpellType<?> getSpell(ItemStack stack){
        if (!stack.hasNbt()) return SpellTypes.EMPTY;

        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains(SPELL_KEY, NbtElement.STRING_TYPE)) return SpellTypes.EMPTY;

        Identifier id = Identifier.tryParse(nbt.getString(SPELL_KEY));
        if (id == null) return  SpellTypes.EMPTY;

        return SpellRegistry.SPELL_TYPES.get(id);
    }
    public static void setSpell(ItemStack stack, SpellType<?> spell) {
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
        SpellType<?> spell = getSpell(stack);
        if (spell.isEmpty()){
             return "item.the-spell-library.scroll.blank";
         }
        return super.getTranslationKey(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }
}
