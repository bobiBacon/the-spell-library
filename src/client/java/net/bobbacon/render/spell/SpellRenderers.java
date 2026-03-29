package net.bobbacon.render.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellDefs;
import net.bobbacon.spell.SpellSchool;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpellRenderers {
    private static final RegistryKey<Registry<SpellRenderer>> SPELL_RENDERER_REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(TheSpellLibrary.MOD_ID, "spell_renderer"));
    public static final SimpleRegistry<SpellRenderer> SPELL_RENDERER = FabricRegistryBuilder.createSimple(SPELL_RENDERER_REGISTRY_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    private static final Map<SpellDef<?>,SpellRenderer> RENDERERS= new HashMap<>();
    private static final RegistryHelper<SpellRenderer> registryHelper= new RegistryHelper<>(SPELL_RENDERER,TheSpellLibrary.MOD_ID);
    public static final SpellRenderer CIRCULAR_AREA_RENDERER= registryHelper.register("circular",new CircularSpellRenderer());
    public static final SpellRenderer AREA_RENDERER= registryHelper.register("area",new AreaSpellRenderer());
    public static final SpellRenderer CONICAL_AREA_RENDERER= registryHelper.register("conical",new ConicalSpellRenderer());

    public static void init(){
        bindRenderer(SpellDefs.DarkAura,CIRCULAR_AREA_RENDERER);
        bindRenderer(SpellDefs.Gust,CONICAL_AREA_RENDERER);
        bindRenderer(SpellDefs.FireWave,CONICAL_AREA_RENDERER);
        bindRenderer(SpellDefs.ExpandedStrength,CIRCULAR_AREA_RENDERER);
    }
    public static void bindRenderer(SpellDef<?> type,SpellRenderer renderer){
        RENDERERS.put(type,renderer);
    }
    public static SpellRenderer getRenderer(SpellDef<?> type) throws UnsupportedOperationException{
        SpellRenderer renderer= RENDERERS.get(type);
        if (renderer==null)throw new UnsupportedOperationException("type "+type.toString()+" has no registered renderer");
        return renderer;
    }

}
