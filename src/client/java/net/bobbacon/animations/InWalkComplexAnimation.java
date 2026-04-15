package net.bobbacon.animations;

import net.bobbacon.TheSpellLibraryClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class InWalkComplexAnimation extends ComplexAnimation{
    long lastSwitch = 0;
    boolean wasMoving = false;


    public InWalkComplexAnimation(AbstractClientPlayerEntity player, List<Identifier> animationIds) {
        super(player,animationIds);
    }

    @Override
    public void start() {
        TheSpellLibraryClient.getAnimManager(player).add(animationIds.get(0),1500);
        double length = player.getVelocity().multiply(1,0,1).length();
        boolean isMoving = length > 0.001;
        if (!isMoving){

        TheSpellLibraryClient.getAnimManager(player).add(animationIds.get(1),1500);
        }

    }

    @Override
    public void end() {
        animationIds.forEach(TheSpellLibraryClient.getAnimManager(player)::remove);
    }

    @Override
    public void tick() {


        double length = player.getVelocity().multiply(1,0,1).length();
        boolean isMoving = length > 0.001;

        if (isMoving != wasMoving ) {
            // switch animation
            lastSwitch = age;
            if (isMoving){
                TheSpellLibraryClient.getAnimManager(player).remove(animationIds.get(1));
            }else {
                TheSpellLibraryClient.getAnimManager(player).add(animationIds.get(1),1500);
            }


        }

        wasMoving = isMoving;
    }
}
