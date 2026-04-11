package net.bobbacon.render.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.spell.CircularAreaSpell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellDefs;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SpellRenderers {
    private static final RegistryKey<Registry<SpellRendererFactory>> SPELL_RENDERER_REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(TheSpellLibrary.MOD_ID, "spell_renderer"));
    public static final SimpleRegistry<SpellRendererFactory> SPELL_RENDERER = FabricRegistryBuilder.createSimple(SPELL_RENDERER_REGISTRY_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    private static final Map<SpellDef<?>,SpellRendererFactory> RENDERER_FACTORIES = new HashMap<>();
    private static final RegistryHelper<SpellRendererFactory> registryHelper= new RegistryHelper<>(SPELL_RENDERER,TheSpellLibrary.MOD_ID);
    public static final SpellRendererFactory CIRCULAR_AREA_RENDERER= registryHelper.register("circular", CircularSpellRenderer::new);
    public static final SpellRendererFactory AREA_RENDERER= registryHelper.register("area",AreaSpellRenderer::new);
    public static final SpellRendererFactory CONICAL_AREA_RENDERER= registryHelper.register("conical",ConicalSpellRenderer::new);

    public static void init(){
        bindRenderer(SpellDefs.DarkAura,CIRCULAR_AREA_RENDERER);
        bindRenderer(SpellDefs.Gust,CONICAL_AREA_RENDERER);
        bindRenderer(SpellDefs.FireWave,CONICAL_AREA_RENDERER);
        bindRenderer(SpellDefs.ExpandedStrength,CIRCULAR_AREA_RENDERER);
    }
    public static void bindRenderer(SpellDef<?> type,SpellRendererFactory renderer){
        RENDERER_FACTORIES.put(type,renderer);
    }
    public static SpellRendererFactory getRendererFactory(SpellDef<?> type) throws UnsupportedOperationException{
        SpellRendererFactory renderer= RENDERER_FACTORIES.get(type);
        if (renderer==null)throw new UnsupportedOperationException("type "+type.toString()+" has no registered renderer");
        return renderer;
    }

}
