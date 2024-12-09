package xyz.faewulf.diversity.util.gameTests.entry.item;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class harvestCrop {
    @GameTest(template = registerGameTests.DEFAULT)
    public void test(GameTestHelper helper) {

        if (ModConfigs.hoe_harvest_crop == ModConfigs.allowHarvestType.DISABLE)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);


        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                helper.setBlock(x, 0, z, Blocks.FARMLAND);
                helper.setBlock(x, 1, z, Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 7));
            }
        }

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(Items.IRON_HOE, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.useBlock(new BlockPos(4, 1, 4), player);
                })
                .thenSucceed();
    }
}
