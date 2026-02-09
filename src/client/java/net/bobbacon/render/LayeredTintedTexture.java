package net.bobbacon.render;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class LayeredTintedTexture {
    public final Identifier textureBase;
    public final ArrayList<Integer> tintLayers= new ArrayList<>();

    public LayeredTintedTexture(Identifier textureBase,Integer... tints) {
        this(textureBase,List.of(tints));
    }

    public LayeredTintedTexture(Identifier textureBase , List<Integer> tints) {
        this.textureBase = textureBase;
        tintLayers.addAll(tints);
    }

    public Identifier getLayerPath(int index){
        return new Identifier(textureBase.getNamespace(),textureBase.getPath()+"_layer"+index);
    }
    public int getHexadecimal(int index){
        return tintLayers.get(index);
    }
    public int getRed(int index){
        return RenderUtils.getRedFromHexa(tintLayers.get(index));
    }
    public int getGreen(int index){
        return RenderUtils.getGreenFromHexa(tintLayers.get(index));
    }
    public int getBlue(int index){
        return RenderUtils.getBlueFromHexa(tintLayers.get(index));
    }

    public int length() {
        return tintLayers.size();
    }
}
