package net.bobbacon.animations;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class AnimationManager {
    protected final AbstractClientPlayerEntity player;
    protected final Map<Identifier, KeyframeAnimationPlayer> activeAnimations=new HashMap<>();
    protected final Map<UUID,ComplexAnimation> activeComplex =new HashMap<>();

    public AnimationManager(AbstractClientPlayerEntity player) {
        this.player = player;
    }

    public void add(Identifier animId, int priority){
        KeyframeAnimation anim = PlayerAnimationRegistry.getAnimation(animId);
        if (anim == null) return;

        KeyframeAnimationPlayer animPlayer = new KeyframeAnimationPlayer(anim);
        activeAnimations.put(animId,animPlayer);
        PlayerAnimationAccess
                .getPlayerAnimLayer(player)
                .addAnimLayer(priority,animPlayer);
    }
    public void remove(Identifier animId){
        PlayerAnimationAccess
                .getPlayerAnimLayer(player)
                .removeLayer(activeAnimations.get(animId));
        activeAnimations.remove(animId);
    }
    public void addComplex(Identifier complexId, UUID slot, List<Identifier> animationIds){
        ComplexAnimation complexAnimation= ComplexAnimations.ComplexAnimationFactories.get(complexId).create(player,animationIds);
        activeComplex.put(slot,complexAnimation);
        complexAnimation.start();
    }
    public void removeComplex(UUID slot){
        activeComplex.get(slot).end();
        activeComplex.remove(slot);
    }
    public void tick(){
        for (ComplexAnimation complexAnimation:activeComplex.values()){
            complexAnimation.age++;
            complexAnimation.tick();
        }
    }
}
