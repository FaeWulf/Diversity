package xyz.faewulf.diversity.registry;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import xyz.faewulf.diversity.util.MissingMethod.ItemStackMethod;
import xyz.faewulf.diversity.util.ModConfigs;

public class CauldronInteractionRegister {
    public static void register() {

        if (!ModConfigs.cauldron_washing_map)
            return;

        CauldronInteraction.WATER.put(Items.FILLED_MAP, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide) {
                ItemStack itemStack1 = new ItemStack(Items.MAP);
                itemStack.setCount(1);

                ItemStackMethod.consume(itemStack, 1, player);

                player.addItem(itemStack1);
                player.awardStat(Stats.USE_CAULDRON);

                LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        });
    }
}
