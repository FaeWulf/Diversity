package faewulf.diversity.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.ItemActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CauldronBehavior.class)
public interface washFilledMapWithCauldron {

    @Inject(method = "registerBehavior", at = @At("TAIL"))
    private static void registerBehaviorMixin(CallbackInfo ci, @Local(ordinal = 1) Map<Item, CauldronBehavior> map) {

        if (!ModConfigs.cauldron_washing_map)
            return;

        map.put(Items.FILLED_MAP, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                ItemStack itemStack = new ItemStack(Items.MAP);
                itemStack.setCount(1);
                stack.decrementUnlessCreative(1, player);

                player.giveItemStack(itemStack);
                player.incrementStat(Stats.USE_CAULDRON);

                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);

                return ItemActionResult.SUCCESS;
            }

            return ItemActionResult.success(world.isClient);
        });

    }
}
