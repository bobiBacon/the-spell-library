package net.bobbacon.ritual;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.spell.SpellType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class RitualManager extends PersistentState {

    private static final RegistryKey<Registry<Ritual>>RITUAL_REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(TheSpellLibrary.MOD_ID, "ritual"));
    public static final SimpleRegistry<Ritual> REGISTRY = FabricRegistryBuilder.createSimple(RITUAL_REGISTRY_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    private static final String NAME = "ritual_manager";
    private static final String RITUALS_KEY = "rituals";
    private final HashMap<UUID, Ritual> rituals = new HashMap<>();
    private static final String TICK_KEY = "tick";
    public int currentTime=0;

    public World world;
    private final static String ENTITY_MAPPING_KEY= "entity_mapping";
    public final Map<UUID,UUID> entityMapping= new HashMap<>();

    private RitualManager(ServerWorld world) {
        this.world =world;
    }
    public static void init(){

    }
    public void tick(){
        Iterator<Ritual> it= this.rituals.values().iterator();
        while (it.hasNext()){
            try {
                Ritual ritual= it.next();
                ritual.tick();
            }catch (Exception e){
                TheSpellLibrary.LOGGER.error(e.toString());
            }

        }
        if (currentTime%200==0){
            markDirty();
        }
    }
    public void add(Ritual ritual){
        rituals.put(ritual.id,ritual);
        markDirty();
    }
    public void remove(Ritual ritual){
        remove(ritual.id);
    }
    public void remove(UUID id){
        TheSpellLibrary.LOGGER.info("removing id"+ id);
        Ritual ritual= rituals.get(id);
        if (ritual!=null){
            ritual.started=false;
        }
        rituals.remove(id);
        markDirty();
    }
    public static RitualManager fromNbt(ServerWorld world, NbtCompound nbt) {
        RitualManager ritualManager = new RitualManager(world);
        ritualManager.currentTime= nbt.getInt(TICK_KEY);
        NbtList nbtList = nbt.getList(RITUALS_KEY, NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Ritual type= REGISTRY.get(Identifier.tryParse(nbtCompound.getString("type_id")));
            if (type == null) {
                TheSpellLibrary.LOGGER.error("Error loading ritual : not valid type {}", nbtCompound.get("type_id"));
                continue;
            }
            Ritual ritual = type.create(world, nbtCompound);
            ritualManager.rituals.put(ritual.id, ritual);
        }
        NbtCompound entities= nbt.getCompound(ENTITY_MAPPING_KEY);
        for (String s:entities.getKeys()) {
            try {
                UUID entityId=UUID.fromString(s);
                ritualManager.entityMapping.put(entityId,entities.getUuid(s));
            }catch (Exception ignored){

            }

        }
        return ritualManager;
    }
    public void onEntityDeath(LivingEntity entity){
        TheSpellLibrary.LOGGER.info("manager onEntityDeath");
        UUID id= entityMapping.get(entity.getUuid());
        if (id!=null){
            Ritual ritual=rituals.get(id);
            if (ritual!=null){
                ritual.onEntityDeath(entity.getUuid());
            }
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt(TICK_KEY, this.currentTime);
        NbtList nbtList = new NbtList();

        for (Ritual ritual : this.rituals.values()) {
            NbtCompound nbtCompound = new NbtCompound();
            ritual.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        NbtCompound entities= new NbtCompound();
        for (Map.Entry<UUID,UUID> entry : entityMapping.entrySet()){
            entities.putUuid(entry.getKey().toString(), entry.getValue());
        }
        nbt.put(ENTITY_MAPPING_KEY,entities);
        nbt.put(RITUALS_KEY, nbtList);
        return nbt;
    }
    public static RitualManager get(ServerWorld world) {
        return world.getPersistentStateManager()
                .getOrCreate(nbt -> RitualManager.fromNbt(world, nbt), () -> new RitualManager(world),NAME);
    }


}
