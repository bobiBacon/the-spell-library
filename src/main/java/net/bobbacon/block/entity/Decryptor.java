package net.bobbacon.block.entity;

import net.bobbacon.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Decryptor extends BlockEntity {
    protected DefaultedList<ItemStack> items= DefaultedList.ofSize(1,ItemStack.EMPTY);
    public float endProgress=0;
    public static final String STATE_KEY ="current_state";
    public State state= State.IDLE;


    public Decryptor(BlockPos pos, BlockState state) {
        super(ModBEs.DECRYPTOR, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, Decryptor decryptor) {
        if (decryptor.state==State.DECRYPTING&&world.getTimeOfDay()%60==0){
            Random random= new Random();
            if (random.nextFloat()>=0.66){
                      decryptor.decrypt();
            }
        }
        if (decryptor.isInEndAnimation()){
            decryptor.endProgress+=0.01f;
            if (decryptor.endProgress>=1){
                decryptor.state=State.IDLE_DECRYPTED;
                decryptor.markDirtyAndSync();
            }
        }
    }

    public ItemStack getStack() {
        return items.get(0);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt,items);
        int stateInt= nbt.getInt(STATE_KEY);
        state= switch (stateInt){
            case 0 ->State.IDLE;
            case 1 ->State.IDLE_DECRYPTED;
            case 2 ->State.DECRYPTING;
            case 3 ->State.TRANSITION;
            default -> throw new IllegalStateException("Unexpected value: " + stateInt);
        };
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.items, true);
        nbt.putInt(STATE_KEY,state.toInt());
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack playerStack= player.getStackInHand(hand);
        ItemStack stack= this.getStack();
        if (stack.isEmpty()&&playerStack.isOf(ModItems.SCROLL)){
            this.setStack(playerStack.split(1));
            startDecrypting();
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        } else if (playerStack.isEmpty() || ItemStack.canCombine(playerStack, stack)) {
            player.giveItemStack(stack);
            stack.decrement(1);
            setStack(stack);
            this.state= State.IDLE;
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
    public boolean isDecrypting(){
        return state==State.DECRYPTING;
    }
    public boolean startDecrypting(){
        boolean isDecrypting=items.get(0).isOf(ModItems.SCROLL);

        if (isDecrypting){
            state= State.DECRYPTING;
            endProgress=0f;
            markDirtyAndSync();
        }
        return isDecrypting;
    }
    public void decrypt(){
        state= State.TRANSITION;

        markDirtyAndSync();
    }
    public float endAnimationProgress(){
        return Math.min(endProgress,1);
    }
    public boolean isInEndAnimation(){
        return state==State.TRANSITION;
    }
    @Override
    @Nullable
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    public void markDirtyAndSync() {
        markDirty();

        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }
    public void setStack(ItemStack stack){
        items.set(0,stack);
    }

    public boolean isIdle() {
        return state==State.IDLE||state==State.IDLE_DECRYPTED;
    }

    public enum State{
        IDLE_DECRYPTED(1),
        IDLE(0),
        DECRYPTING(2),
        TRANSITION(3);
        final int id;
        State(int id) {
            this.id=id;
        }

        public int toInt(){
            return id;
        }
    }
}
