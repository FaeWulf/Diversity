package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class tickDelayCopperBlock {

    @GameTest(template = registerGameTests.DEFAULT)
    public void test(GameTestHelper helper) {

        helper.setBlock(4, 1, 4, Blocks.COPPER_BULB);
        helper.setBlock(4, 2, 4, Blocks.BAMBOO_BUTTON);

        if (!ModConfigs.copper_bulb_tick_delay)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.startSequence()
                .thenExecute(() -> helper.pressButton(4, 2, 4))
                .thenExecute(() -> helper.assertBlockProperty(new BlockPos(4, 1, 4), BlockStateProperties.POWERED, false))
                .thenExecuteAfter(1, () -> helper.assertBlockProperty(new BlockPos(4, 1, 4), BlockStateProperties.POWERED, true))
                .thenSucceed();
    }
}
