package xyz.faewulf.diversity.mixin.core.CustomBonemealable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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

    @Inject(method = {"growCrop"}, at = @At(value = "HEAD"), cancellable = true)
    private static void growCropInject(ItemStack stack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockstate = level.getBlockState(pos);
        if (blockstate.getBlock() instanceof ICustomBonemealable iCustomBonemealable && iCustomBonemealable.Diversity$isValidBonemealTarget(level, pos, blockstate)) {
            if (level instanceof ServerLevel) {
                if (iCustomBonemealable.Diversity$isBonemealSuccess(level, level.getRandom(), pos, blockstate)) {
                    iCustomBonemealable.Diversity$performBonemeal((ServerLevel) level, level.getRandom(), pos, blockstate);
                }

                stack.shrink(1);
            }

            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    // For forge and neoforge, they replaced vanilla code with their code which... doing the same thing? What? Why?
    @Inject(method = {"applyBonemeal"}, at = @At(value = "HEAD"), cancellable = true, require = 0)
    private static void growCropInjectForge(ItemStack stack, Level level, BlockPos pos, Player player, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockstate = level.getBlockState(pos);
        if (blockstate.getBlock() instanceof ICustomBonemealable iCustomBonemealable && iCustomBonemealable.Diversity$isValidBonemealTarget(level, pos, blockstate)) {
            if (level instanceof ServerLevel) {
                if (iCustomBonemealable.Diversity$isBonemealSuccess(level, level.getRandom(), pos, blockstate)) {
                    iCustomBonemealable.Diversity$performBonemeal((ServerLevel) level, level.getRandom(), pos, blockstate);
                }

                stack.shrink(1);
            }

            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    //server side swing
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V", ordinal = 0))
    private void useOnInject(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getPlayer() != null)
            context.getPlayer().swing(context.getHand(), true);
    }
}
