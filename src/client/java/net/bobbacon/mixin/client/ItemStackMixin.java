package net.bobbacon.mixin.client;

import net.bobbacon.item.ModItems;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
@Environment(EnvType.CLIENT)
public class ItemStackMixin {
    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void changeName(CallbackInfoReturnable<Text> cir) {
        ItemStack self = (ItemStack) (Object) this;

        if (!self.isOf(ModItems.SCROLL)) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        SpellType<?> spell = ScrollItem.getSpell(self);
        if (spell == null||spell.isEmpty()) return;

        if (ScrollItem.canRead(client.player, self)){
            cir.setReturnValue(Text.translatable("item.night-of-the-dead.scroll.spell."+ SpellRegistry.SPELL_TYPES.getId(spell).getPath()));
        }
    }
}
