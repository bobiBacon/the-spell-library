package net.bobbacon.ritual;

import net.bobbacon.Accessors.EntityAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Ritual {
    private static final String ID_KEY= "id";
    public UUID id;
    public BlockPos center;
    private static final String CENTER_X_KEY = "x";
    private static final String CENTER_Y_KEY = "y";
    private static final String CENTER_Z_KEY = "z";

    public World world;
    private static final String STARTED_KEY = "started";
    public boolean started= false;
    private static final String PHASE_KEY = "phase";
    byte phaseCount = -1;
    protected final ArrayList<Phase> phases = new ArrayList<>();
    private static final String TIME_KEY="time";
    public int time=0;
    private static final String IS_PHASE_INIT_KEY= "is_phase_init";
    boolean isPhaseInit = false;
    private static final String ENTITY_LIST_KEY = "entities";
    public final ArrayList<UUID> entities= new ArrayList<>();
    private static final String PLAYER_LIST_KEY = "players";

    public final ArrayList<UUID> players= new ArrayList<>();
    /**Only for optimisation.
     * For real check, please take a look to areEntitiesAlive().*/
    protected int entityCount=0;





    public Ritual(BlockPos center, World world) {
        this.center = center;
        this.world = world;
        this.id= UUID.randomUUID();
        definePhases();
        markDirty();
    }
    public Ritual(World world, NbtCompound nbt){
        this.world = world;
        definePhases();
        readNbt(nbt);
    }
    /**This should redirect to the class constructor using nbt*/
    public abstract Ritual create(World world, NbtCompound nbtCompound);
    public abstract void definePhases();
    /**only evaluated on server side*/
    protected void tick(){
        if (started){
            this.time++;
        }else return;
        if (phases.get(phaseCount).tick(time)){
            nextPhase();
        }

    }

    public abstract boolean hasRitualSite();
    public boolean tryStart(){
        if (world.isClient){
            return false;
        }
        started = canStart();
        if (started){
            definePlayers();
            RitualManager ritualManager = RitualManager.get((ServerWorld) world);
            ritualManager.add(this);
            start();
            nextPhase();
        }
        return started;
    }
    public void start(){

    }
    public boolean canStart(){
        return hasRitualSite();
    }
    protected void definePlayers(){
        List<PlayerEntity> list= world.getEntitiesByType(EntityType.PLAYER,new Box(center.west(15).south(15).down(4), center.east(15).north(15).up(6)), entity -> !entity.isSpectator());
        for (PlayerEntity player: list){
            players.add(player.getUuid());
        }
    }
    public List<PlayerEntity> getPlayers(){
        ArrayList<PlayerEntity> list= new ArrayList<>();
        for (UUID id: players){
            PlayerEntity player= getPlayer(id);
            if (player!=null){
                list.add(getPlayer(id));
            }
        }
        return list;
    }
    @Nullable
    public PlayerEntity getPlayer(UUID id){
        if (((ServerWorld)world).getEntity(id) instanceof PlayerEntity entity){
            return entity;
        }
        return null;
    }

    public void nextPhase(){
        if (started){
            if (phaseCount+1<phases.size()){
                phaseCount++;
                phases.get(phaseCount).start();
                time=0;
            }else {
                complete();
            }
            markDirty();
        }
    }

    protected void complete() {
        if (world.isClient){
            return;
        }
        abort();
    }
    /**Terminates the ritual whenever complete or not*/
    public void abort(){
        RitualManager.get((ServerWorld) world).remove(this);
    }

    public void readNbt(NbtCompound nbt) {
        id= nbt.getUuid(ID_KEY);
        phaseCount = nbt.getByte(PHASE_KEY);
        started= nbt.getBoolean(STARTED_KEY);
        center= new BlockPos(nbt.getInt(CENTER_X_KEY),nbt.getInt(CENTER_Y_KEY),nbt.getInt(CENTER_Z_KEY));
        time= nbt.getInt(TIME_KEY);
        isPhaseInit=nbt.getBoolean(IS_PHASE_INIT_KEY);
        NbtList list= nbt.getList(ENTITY_LIST_KEY, NbtElement.STRING_TYPE);
        for (NbtElement e:list){
            if (e instanceof NbtString s){
                entities.add(UUID.fromString(s.asString()));
            }
        }
        entityCount=entities.size();
        NbtList list1= nbt.getList(PLAYER_LIST_KEY, NbtElement.STRING_TYPE);
        for (NbtElement e:list1){
            if (e instanceof NbtString s){
                players.add(UUID.fromString(s.asString()));
            }
        }
    }

    protected void writeNbt(NbtCompound nbt) {
        nbt.putUuid(ID_KEY,id);
        nbt.putByte(PHASE_KEY, phaseCount);
        nbt.putBoolean(STARTED_KEY,started);
        nbt.putInt(CENTER_X_KEY,center.getX());
        nbt.putInt(CENTER_Y_KEY,center.getY());
        nbt.putInt(CENTER_Z_KEY,center.getZ());
        nbt.putInt(TIME_KEY,time);
        nbt.putBoolean(IS_PHASE_INIT_KEY,isPhaseInit);
        NbtList list= new NbtList();
        for (UUID id: entities){
            list.add(NbtString.of(id.toString()));
        }
        nbt.put(ENTITY_LIST_KEY,list);
        NbtList list1= new NbtList();
        for (UUID id: players){
            list1.add(NbtString.of(id.toString()));
        }
        nbt.put(PLAYER_LIST_KEY,list1);
    }
    public void markDirty(){
        if (!world.isClient){
            RitualManager.get((ServerWorld) world).markDirty();
        }
    }
    public boolean shouldInitPhase(){
        if (!isPhaseInit){
            isPhaseInit =true;
            markDirty();
            return true;
        }
        return false;
    }
    protected void onEntityDeath(UUID id){
        if (entities.remove(id)){
            entityCount--;
        }
        markDirty();
    }

    /**returns whenever one entity or more linked to this ritual is alive*/
    public boolean areEntitiesAlive(){
        for (UUID id: entities){
            Entity entity= ((ServerWorld)world).getEntity(id);
            if (entity!=null&&entity.isAlive()){
                return true;
            }
        }
        return false;
    }
    protected void spawnEntity(LivingEntity entity){
        world.spawnEntity(entity);
        RitualManager.get((ServerWorld) world).entityMapping.put(entity.getUuid(),this.id);
        entities.add(entity.getUuid());
        entityCount++;
        markDirty();
    }
    protected void spawnMobInRange(MobEntity entity,float radius,int targetingDistance){
        float diameter= 2f* radius;
        BlockPos pos= center.west(Math.round(world.random.nextFloat()*diameter-radius)).north(Math.round(world.random.nextFloat()*diameter-radius));
        while (!SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND,world,pos, EntityType.SKELETON)){
            pos= center.west(Math.round(world.random.nextFloat()*diameter-radius)).north(Math.round(world.random.nextFloat()*diameter-radius));
        }
        spawnMob(entity,pos,targetingDistance);
    }
    protected void spawnMob(MobEntity entity, BlockPos pos, int targetingDistance){
        entity.setPos(pos.getX(),pos.getY(),pos.getZ());
        entity.targetSelector.add(
                1,
                new ActiveTargetGoal<>(
                        entity,
                        PlayerEntity.class,
                        targetingDistance,
                        false,
                        false,
                        player -> players.contains(player.getUuid())
                )
        );
        ((EntityAccessor)entity).the_spell_library$setComesFromRitual(true,id);
        entity.setPersistent();
        spawnEntity(entity);
    }
    public static boolean checkGround(BlockPos groundCenter, int size, Block block,World world){
        int max= (int) Math.ceil(size/2f);
        int min= (int) Math.ceil(-size/2f);
        for (int i = -min; i <max ; i++) {
            for (int j = -min; j < max; j++) {
                BlockPos pos= groundCenter.east(i).north(j);
                if (!world.getBlockState(pos).isOf(block)){
                    return false;
                }
            }
        }
        return true;
    }
}
