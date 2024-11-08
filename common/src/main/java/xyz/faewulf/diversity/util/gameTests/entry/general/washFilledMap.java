package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class washFilledMap {
    @GameTest(template = registerGameTests.DEFAULT)
    public void test(GameTestHelper helper) {

        if (!ModConfigs.cauldron_washing_map)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.setBlock(4, 2, 4, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 3));
        Player player = helper.makeMockSurvivalPlayer();

        ItemStack itemStack = new ItemStack(Items.FILLED_MAP, 1);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> helper.useBlock(new BlockPos(4, 2, 4), player))
                .thenExecute(() -> {
                    if (!player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.MAP))
                        helper.fail("Holding " + player.getItemInHand(InteractionHand.MAIN_HAND).getDisplayName().getString());
                })
                .thenSucceed();
    }
}
