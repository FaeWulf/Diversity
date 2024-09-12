package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import xyz.faewulf.diversity.util.config.ModConfigs;


public class useShearOnBlock {
    public static InteractionResult run(Level world, Player player, InteractionHand hand, BlockHitResult hitResult) {


        if (!ModConfigs.check_villager_schedule)
            return InteractionResult.PASS;

        if (world.isClientSide)
            return InteractionResult.PASS;

        Item item = player.getItemInHand(hand).getItem();

        if (item == Items.SHEARS) {

            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

            //sugar cane
            if (blockState.getBlock() instanceof SugarCaneBlock) {

                IntegerProperty AGE = SugarCaneBlock.AGE;
                world.setBlock(blockPos, blockState.setValue(AGE, 15), 4);

                return InteractionResult.CONSUME;
            }
        }


        return InteractionResult.PASS;
    }
}
