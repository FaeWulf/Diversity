package xyz.faewulf.diversity.mixin;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.MissingMethod.ItemStackMethod;
import xyz.faewulf.diversity.util.ModConfigs;

import static net.minecraft.core.cauldron.CauldronInteraction.WATER;

@Mixin(CauldronInteraction.class)
public interface washFilledMapWithCauldron {

    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void registerBehaviorMixin(CallbackInfo ci) {

        if (!ModConfigs.cauldron_washing_map)
            return;

        WATER.put(Items.FILLED_MAP, (state, world, pos, player, hand, stack) -> {
            if (!world.isClientSide) {
                ItemStack itemStack = new ItemStack(Items.MAP);
                itemStack.setCount(1);

                ItemStackMethod.consume(stack, 1, player);

                player.addItem(itemStack);
                player.awardStat(Stats.USE_CAULDRON);

                LayeredCauldronBlock.lowerFillLevel(state, world, pos);

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        });

    }
}
