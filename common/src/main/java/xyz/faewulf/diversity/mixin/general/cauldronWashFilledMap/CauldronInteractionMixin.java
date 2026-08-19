package xyz.faewulf.diversity.mixin.general.cauldronWashFilledMap;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(CauldronInteractions.class)
public class CauldronInteractionMixin {

    @Shadow
    @Final
    public static CauldronInteraction.Dispatcher WATER;

    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void bootStrapInject(CallbackInfo ci) {

        //Todo: config should warn about restart the game
        if (!ModConfigs.cauldron_washing_map)
            return;

        ((CauldronInteractionDispatcherInvoker) WATER).diversity$getPut(
                Items.FILLED_MAP, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
                    if (!level.isClientSide()) {
                        ItemStack itemStack1 = new ItemStack(Items.MAP);

                        itemStack1.setCount(1);

                        itemStack.consume(1, player);

                        player.addItem(itemStack1);
                        player.awardStat(Stats.USE_CAULDRON);

                        LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                    }

                    return InteractionResult.SUCCESS;
                }
        );
    }
}
