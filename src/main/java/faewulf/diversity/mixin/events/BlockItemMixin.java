package faewulf.diversity.mixin.events;

import faewulf.diversity.callback.AttemptedBlockPlaceCallback;
import faewulf.diversity.callback.BlockPlacedCallback;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    //event onBlockPlaceAttempt
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void onBlockAttemptedPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        ActionResult result = AttemptedBlockPlaceCallback.EVENT.invoker().onBlockAttemptedPlace(context);
        if (result != ActionResult.PASS) {
            cir.setReturnValue(result);
        }
    }


    //? if >=1.21 {
    
    /*//event onBlockPlaced
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"))
    private void onBlockPlaced(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        BlockPlacedCallback.EVENT.invoker().onBlockPlaced(context);
    }
    
    *///?}

    //? if =1.20.4 {
    /*//event onBlockPlaced
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V"))
    private void onBlockPlaced(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        BlockPlacedCallback.EVENT.invoker().onBlockPlaced(context);
    }
    *///?}
}
