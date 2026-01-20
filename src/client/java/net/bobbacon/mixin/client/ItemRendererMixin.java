package net.bobbacon.mixin.client;

import net.bobbacon.TheSpellLibraryClient;
import net.bobbacon.item.ModItems;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.render.item.ScrollItemRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",at = @At("HEAD"),argsOnly = true)
    public BakedModel parchmentRender(BakedModel value, ItemStack stack, ModelTransformationMode renderMode){
        if (stack.isOf(ModItems.SCROLL)&&renderMode != ModelTransformationMode.GUI && renderMode != ModelTransformationMode.FIXED && renderMode != ModelTransformationMode.GROUND ){
            BakedModel model = ((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(TheSpellLibraryClient.MOD_ID,"scroll_3d","inventory"));
            return model;
        }
        return value;
    }
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("RETURN"))
    private void renderSymbol(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci){

        if (stack.isOf(ModItems.SCROLL)&&ScrollItem.canRead(MinecraftClient.getInstance().player,stack)){
            if (renderMode == ModelTransformationMode.GUI) {
                ScrollItemRenderer.renderSpellSymbolGui(stack, matrices, vertexConsumers, overlay);
            }else if (renderMode==ModelTransformationMode.FIRST_PERSON_RIGHT_HAND||renderMode==ModelTransformationMode.FIRST_PERSON_LEFT_HAND){
                ScrollItemRenderer.renderSymbol(stack,matrices,vertexConsumers,light,leftHanded,renderMode,model);
            }
        }
    }
}
