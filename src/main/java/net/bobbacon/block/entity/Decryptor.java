package net.bobbacon.block.entity;

import net.bobbacon.block.DecryptorBlock;
import net.bobbacon.item.ModItems;
import net.bobbacon.item.ScrollItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Decryptor extends BlockEntity implements Inventory {


    protected DefaultedList<ItemStack> items= DefaultedList.ofSize(1,ItemStack.EMPTY);
    protected float endProgress=0;
    public static final String STATE_KEY ="current_state";
    public State state= State.IDLE;
//    static {
//        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((BlockEntity entity, ServerWorld world)->{
//            if (entity instanceof Decryptor decryptor){
//                if (decryptor.isInEndAnimation()){
//                    decryptor.setState(State.IDLE_DECRYPTED);
//                }
//                if (decryptor.isDecrypted()){
//                    decryptor.endProgress=1;
//                }
////                decryptor.markDirtyAndSync();
//            }
//        });
//    }

    public Decryptor(BlockPos pos, BlockState state) {
        super(ModBEs.DECRYPTOR, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, Decryptor decryptor) {

        if (decryptor.isDecrypting()&&world.getTimeOfDay()%400==0){
            if (world.isClient){
                return;
            }
            Random random= new Random();
            if (random.nextFloat()>=0.66f){
                decryptor.startTransition(state);
            }
        }
        if (decryptor.isInEndAnimation()){
            decryptor.endProgress+=0.01f;
            if (decryptor.endProgress>=1){
                decryptor.decrypt(state);
            }
        }
    }

    public void decrypt(BlockState state){
        this.setState(State.IDLE_DECRYPTED,state);
        this.markDirtyAndSync();
    }
    public void setState(State state,BlockState blockState){
        this.state= state;
        world.setBlockState(pos, blockState.with(Properties.LIT,isDecrypting()||isInEndAnimation()), Block.NOTIFY_ALL);
        this.markDirtyAndSync();
    }

    public ItemStack getStack() {
        return items.get(0);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt,items);
        int stateInt= nbt.getInt(STATE_KEY);
        state=State.fromInt(stateInt);
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
            startDecrypting(state);
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        } else if (playerStack.isEmpty() || ItemStack.canCombine(playerStack, stack)) {
            if (isDecrypted()){
                ScrollItem.decrypt(player,stack);
            }
            player.giveItemStack(stack);
            stack.decrement(1);
            setStack(stack);
            setState(State.IDLE,state);
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public boolean isDecrypting(){
        return state==State.DECRYPTING;
    }
    public boolean startDecrypting(BlockState blockState){
        boolean isDecrypting=items.get(0).isOf(ModItems.SCROLL);

        if (isDecrypting){
            setState(State.DECRYPTING,blockState);
            endProgress=0f;
            markDirtyAndSync();
        }
        return isDecrypting;
    }
    public void startTransition(BlockState blockState){
        setState(State.TRANSITION,blockState);
        markDirtyAndSync();
    }
    public float endAnimationProgress(){

        return isDecrypted()?1f:Math.min(endProgress,1);
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
    public boolean isDecrypted(){
        return state==State.IDLE_DECRYPTED;
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack=items.get(slot);
        stack.decrement(amount);
        return items.set(slot,stack);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return items.remove(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot,stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.clear();
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
        public static State fromInt(int id){
            return switch (id){
                case 0 ->State.IDLE;
                case 1 ->State.IDLE_DECRYPTED;
                case 2 ->State.DECRYPTING;
                case 3 ->State.TRANSITION;
                default -> throw new IllegalStateException("Unexpected value: " + id);
            };
        }
    }
}
