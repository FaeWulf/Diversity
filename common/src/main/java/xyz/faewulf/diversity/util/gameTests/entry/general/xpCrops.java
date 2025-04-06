package xyz.faewulf.diversity.util.gameTests.entry.general;


import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.DEFAULT;

import java.util.List;

@TestGroup
public class xpCrops {
    //@GameTest(template = DEFAULT, timeoutTicks = 200)
    public void test(GameTestHelper helper) {

        if (!ModConfigs.xp_crops)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                helper.setBlock(i, 1, j, Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 7));
                helper.setBlock(i, 0, j, Blocks.FARMLAND);
            }
        }

        helper.setBlock(4, 0, 4, Blocks.COPPER_GRATE.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true));

        helper.startSequence()
                .thenExecute(() -> {
                    helper.setBlock(4, 1, 4, Blocks.WATER);
                })
                .thenExecuteAfter(20 * 5, () -> {
                    List<ExperienceOrb> experienceOrb = helper.getEntities(EntityType.EXPERIENCE_ORB);

                    if (experienceOrb.isEmpty())
                        helper.fail(Component.literal("No xp orbs dropped"));
                })
                .thenSucceed();
    }
}
