package net.bobbacon.utils;

import net.minecraft.util.math.BlockPos;

import java.util.*;

public class Utils {
    public static List<BlockPos> getSphere(BlockPos center, float radius){
        List<BlockPos> list= new ArrayList<>();
        list.addAll(getCircle(center,radius));

        for (int i = 1; i < radius; i++) {
            float circleRadius= (float) Math.sqrt(radius*radius-i*i);
            list.addAll(getCircle(center.up(i),circleRadius));
            list.addAll(getCircle(center.down(i),circleRadius));
        }
        return list;
    }
    public static List<BlockPos> getCircle(BlockPos center, float radius){
        int x= center.getX();
        int y= center.getY();
        int z= center.getZ();

        ArrayList<BlockPos> blocks= new ArrayList<>();
        for (float i = 1.5f; i <radius; i++) {
            for (float j = 1.5f; j < radius; j++) {

                if (i*i+j*j <= radius*radius){
                    int x1 = (int) (x+Math.floor(i));
                    int y1 = (int) (z+Math.floor(j));
                    int x2 = (int) (x-Math.floor(i));
                    int y2 = (int) (z-Math.floor(j));
                    blocks.add(new BlockPos(x1, y,y1));
                    blocks.add(new BlockPos(x2, y,y1));
                    blocks.add(new BlockPos(x1, y,y2));
                    blocks.add(new BlockPos(x2, y,y2));
                }
            }
        }
        for (int i=-Math.round(radius)+1; i<Math.round(radius); i++){
            blocks.add(new BlockPos(x +i, y, z));
            blocks.add(new BlockPos(x, y, z +i));
        }
        return blocks;
    }
    public static List<BlockPos> getRingForm(BlockPos center, int innerRadius, int outerRadius){
        if (outerRadius==innerRadius){
            return new ArrayList<>();
        }
        if (innerRadius==0){
            return getCircle(center,outerRadius);
        }
        if (outerRadius<innerRadius){
            int i=innerRadius;
            innerRadius= outerRadius;
            outerRadius=i;
        }
        List<BlockPos> inner = getCircle(center,innerRadius);
        List<BlockPos> outer = getCircle(center,outerRadius);
        for (BlockPos pos: inner){
            outer.remove(pos);
        }
        return outer;
    }
    public static <T> T weightedRandom(Map<T,Integer> choices) {
        int totalWeight= 0;
        Map<T,Integer> startingNumbers = new HashMap<>();
        for(Map.Entry<T,Integer> entry:choices.entrySet()) {
            startingNumbers.put(entry.getKey(), totalWeight);
            totalWeight += choices.get(entry.getKey());
        }
        if (totalWeight <= 0) {
            throw new RuntimeException("no choices present in weighted random");
        }
        Random random= new Random();
        int number = random.nextInt(totalWeight);
        int cumulative = 0;
        for (Map.Entry<T,Integer> entry:choices.entrySet()) {
            cumulative += entry.getValue();
            if (number < cumulative) {
                return entry.getKey();
            }
        }
        return null;
    }
}
