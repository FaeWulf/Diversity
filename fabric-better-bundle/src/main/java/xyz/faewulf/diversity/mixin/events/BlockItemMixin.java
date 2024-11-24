package xyz.faewulf.diversity.mixin.events;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.callback.AttemptedBlockPlaceCallback;
import xyz.faewulf.diversity.callback.BlockPlacedCallback;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    //event onBlockPlaceAttempt
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void onBlockAttemptedPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult result = AttemptedBlockPlaceCallback.EVENT.invoker().onBlockAttemptedPlace(context);
        if (result != InteractionResult.PASS) {
            cir.setReturnValue(result);
            cir.cancel();
        }
    }

    //event onBlockPlaced
    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
    private void onBlockPlaced(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPlacedCallback.EVENT.invoker().onBlockPlaced(context);
    }

}
