package net.bobbacon.animations;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class ComplexAnimation {
    public int age=0;
    protected final List<Identifier> animationIds;
    public final AbstractClientPlayerEntity player;

    public ComplexAnimation(AbstractClientPlayerEntity player,List<Identifier> animationIds) {
        this.animationIds = animationIds;
        this.player = player;
    }
    public abstract void start();
    public abstract void end();
    public abstract void tick();


    public interface Factory{
        ComplexAnimation create(AbstractClientPlayerEntity player,List<Identifier> animationIds);
    }
}
