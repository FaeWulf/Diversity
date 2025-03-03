package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.DEFAULT;

@TestGroup
public class deepslateGenerator {
    @GameTest(template = DEFAULT)
    public void test_cobble(GameTestHelper helper) {

        helper.setBlock(4, 2, 4, Blocks.WATER);
        helper.setBlock(4, 4, 4, Blocks.LAVA);
        helper.setBlock(1, 3, 1, Blocks.LAVA);

        if (!ModConfigs.deepslate_generator)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.runAfterDelay(20 * 5, () -> {
            helper.succeedIf(() -> {
                helper.assertBlockPresent(Blocks.COBBLESTONE, new BlockPos(1, 2, 1));
                helper.assertBlockPresent(Blocks.STONE, new BlockPos(4, 2, 4));
            });
        });
    }

    @GameTest(template = DEFAULT)
    public void test_deepslate(GameTestHelper helper) {

        if (!ModConfigs.deepslate_generator)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.setBlock(4, 2, 4, Blocks.WATER);
        helper.setBlock(4, 4, 4, Blocks.LAVA);
        helper.setBlock(1, 3, 1, Blocks.LAVA);

        helper.runAfterDelay(20 * 5, () -> {
            helper.succeedIf(() -> {
                helper.assertBlockPresent(Blocks.COBBLED_DEEPSLATE, new BlockPos(1, 2, 1));
                helper.assertBlockPresent(Blocks.DEEPSLATE, new BlockPos(4, 3, 4));
            });
        });
    }
}
