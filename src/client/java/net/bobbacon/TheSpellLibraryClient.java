package net.bobbacon;

import net.bobbacon.item.ScrollItem;
import net.bobbacon.render.blockEntity.BlockEntityRenderers;
import net.bobbacon.spell.SpellType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;

public class TheSpellLibraryClient implements ClientModInitializer {
    public static final String MOD_ID = "the-spell-library";

    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BlockEntityRenderers.init();
        ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
            if (!(stack.getItem() instanceof ScrollItem)) return;

            SpellType<?> spell = ScrollItem.getSpell(stack);
            if (spell == null||spell.isEmpty()) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            //TODO descriptions
            if (ScrollItem.canRead(client.player,stack)){
//				tooltip.add(0,Text.translatable("item.night-of-the-dead.scroll.spell."+ModRegistries.SPELL_TYPES.getId(spell).getPath()));
            } else {
//				tooltip.add(0,Text.translatable("item.night-of-the-dead.scroll.spell.unknown"));
            }
        });
	}
}