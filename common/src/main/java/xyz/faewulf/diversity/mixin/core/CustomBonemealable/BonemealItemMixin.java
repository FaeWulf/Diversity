package xyz.faewulf.diversity.mixin.core.CustomBonemealable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.inter.ICustomBonemealable;

@Mixin(BoneMealItem.class)
public abstract class BonemealItemMixin extends Item {
    public BonemealItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "growCrop", at = @At(value = "HEAD"), cancellable = true)
    private static void growCropInject(ItemStack stack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockstate = level.getBlockState(pos);
        if (blockstate.getBlock() instanceof ICustomBonemealable iCustomBonemealable && iCustomBonemealable.Diversity$isValidBonemealTarget(level, pos, blockstate)) {
            if (level instanceof ServerLevel) {
                if (iCustomBonemealable.Diversity$isBonemealSuccess(level, level.random, pos, blockstate)) {
                    iCustomBonemealable.Diversity$performBonemeal((ServerLevel) level, level.random, pos, blockstate);
                }

                stack.shrink(1);
            }

            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    //server side swing
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/context/UseOnContext;getPlayer()Lnet/minecraft/world/entity/player/Player;", ordinal = 0))
    private void useOnInject(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getPlayer() != null)
            context.getPlayer().swing(context.getHand(), true);
    }
}
