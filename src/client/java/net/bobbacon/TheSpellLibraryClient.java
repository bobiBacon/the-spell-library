package net.bobbacon;

import net.bobbacon.Accessors.PlayerAccessor;
import net.bobbacon.Accessors.WorldAccessor;
import net.bobbacon.accessor.ClientWorldAccessor;
import net.bobbacon.animations.AnimationManager;
import net.bobbacon.animations.ComplexAnimations;
import net.bobbacon.item.ModItems;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.particles.ModParticlesClient;
import net.bobbacon.render.ModRenderLayers;
import net.bobbacon.render.blockEntity.BlockEntityRenderers;
import net.bobbacon.render.spell.SpellRenderer;
import net.bobbacon.render.spell.SpellRenderers;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.TickedSpell;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.*;

import static net.bobbacon.TheSpellLibrary.*;

public class TheSpellLibraryClient implements ClientModInitializer {
    public static final String MOD_ID = "the-spell-library";
    public static final Map<UUID, AnimationManager> animationManagers=new HashMap<>();
    public static AnimationManager getAnimManager(AbstractClientPlayerEntity player){
        UUID uuid = player.getUuid();
        if (animationManagers.containsKey(uuid)){
            return animationManagers.get(uuid);
        }
        AnimationManager manager = new AnimationManager(player);
        animationManagers.put(uuid, manager);
        return manager;
    }
    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BlockEntityRenderers.init();
        ModRenderLayers.init();
        ModParticlesClient.init();
        SpellRenderers.init();
        ComplexAnimations.init();


        ClientPlayNetworking.registerGlobalReceiver(PLAY_ANIMATION_PACKET, (client, handler, buf, responseSender) -> {

            UUID uuid = buf.readUuid();
            Identifier animationId = buf.readIdentifier();

            client.execute(() -> {
                ClientWorld world = client.world;
                if (world == null) return;

                Entity entity = world.getPlayerByUuid(uuid);
                if (!(entity instanceof AbstractClientPlayerEntity player)) return;

                getAnimManager(player).add(animationId,1);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(STOP_ANIMATION_PACKET, (client, handler, buf, responseSender) -> {

            UUID uuid = buf.readUuid();
            Identifier animationId = buf.readIdentifier();

            client.execute(() -> {
                ClientWorld world = client.world;
                if (world == null) return;

                Entity entity = world.getPlayerByUuid(uuid);
                if (!(entity instanceof AbstractClientPlayerEntity player)) return;

                getAnimManager(player).remove(animationId);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PLAY_COMPLEX_ANIMATION_PACKET, (client, handler, buf, responseSender) -> {

            UUID uuid = buf.readUuid();
            Identifier complexAnimationId = buf.readIdentifier();
            UUID slot= buf.readUuid();
            String ids= buf.readString();
            String[] idArray=ids.split(",");
            List<Identifier> animationIds= new ArrayList<>();
            for (String stringId:idArray){
                Identifier id= Identifier.tryParse(stringId);
                if (id!=null){
                    animationIds.add(id);

                }
            }

            client.execute(() -> {
                ClientWorld world = client.world;
                if (world == null) return;

                Entity entity = world.getPlayerByUuid(uuid);
                if (!(entity instanceof AbstractClientPlayerEntity player)) return;

                getAnimManager(player).addComplex(complexAnimationId,slot,animationIds);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(STOP_COMPLEX_ANIMATION_PACKET, (client, handler, buf, responseSender) -> {

            UUID uuid = buf.readUuid();
            UUID uuid1 = buf.readUuid();

            client.execute(() -> {
                ClientWorld world = client.world;
                if (world == null) return;

                Entity entity = world.getPlayerByUuid(uuid);
                if (!(entity instanceof AbstractClientPlayerEntity player)) return;

                getAnimManager(player).removeComplex(uuid1);
            });
        });
        ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
            if (!(stack.getItem() instanceof ScrollItem)) return;

            SpellDef<?> spell = ScrollItem.getSpell(stack);
            if (spell == null||spell.isEmpty()) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            //TODO descriptions
            if (ScrollItem.canRead(client.player,stack)){
                tooltip.addAll(spell.newSpell(client.world, client.player).getTooltips());
            } else {
            }
        });
        WorldRenderEvents.AFTER_ENTITIES.register(context ->{
            for (TickedSpell tickedSpell: ((WorldAccessor)MinecraftClient.getInstance().world).getSpellTickingManager().getSpells()){
                Spell spell1= (Spell) tickedSpell;
                if (spell1.type.hasRenderer) {
                    SpellRenderer renderer = ((ClientWorldAccessor)MinecraftClient.getInstance().world).getSpellRendererManager().getOrCreate(spell1);
                    MatrixStack matrices = context.matrixStack();
                    Camera camera = context.camera();
                    Vec3d camPos = camera.getPos();
                    matrices.push();
                    matrices.translate(-camPos.x, -camPos.y, -camPos.z);
                    renderer.renderingTick(context,spell1, matrices);
                    matrices.pop();
                }
            }
        });
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {

            PlayerEntity player= MinecraftClient.getInstance().player;
            ItemStack stack=player.getStackInHand(player.getActiveHand());
            if (!stack.isOf(ModItems.SCROLL)){
                return;
            }
//            SpellDef<?> type=ScrollItem.getSpell(stack);
//            Spell spell= type.newSpell(MinecraftClient.getInstance().world, player);
            Spell spell= ((PlayerAccessor)player).getCurrentlyCastingSpell();
            if (spell!=null) {
                if (spell.type.hasRenderer) {
                    SpellRenderer renderer = SpellRenderers.getRendererFactory(spell.type).create();
                    MatrixStack matrices = context.matrixStack();
                    Camera camera = context.camera();
                    Vec3d camPos = camera.getPos();
                    matrices.push();
                    matrices.translate(-camPos.x, -camPos.y, -camPos.z);
                    renderer.renderCasting(context, spell, player, matrices);
                    matrices.pop();
                }
            }

        });
	}

}