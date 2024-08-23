package xyz.faewulf.diversity.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.ModConfigs;

import java.util.Map;

@Mixin(CauldronInteraction.class)
public interface washFilledMapWithCauldron {

    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void registerBehaviorMixin(CallbackInfo ci, @Local(ordinal = 1) Map<Item, CauldronInteraction> map) {

        if (!ModConfigs.cauldron_washing_map)
            return;

        map.put(Items.FILLED_MAP, (state, world, pos, player, hand, stack) -> {
            if (!world.isClientSide) {
                ItemStack itemStack = new ItemStack(Items.MAP);
                itemStack.setCount(1);
                stack.consume(1, player);

                player.addItem(itemStack);
                player.awardStat(Stats.USE_CAULDRON);

                LayeredCauldronBlock.lowerFillLevel(state, world, pos);

                return ItemInteractionResult.SUCCESS;
            }

            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        });

    }
}
