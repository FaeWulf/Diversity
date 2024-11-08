package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class sneakThroughBerrybush {

    @GameTest(template = registerGameTests.DEFAULT)
    public void test(GameTestHelper helper) {
        if (!ModConfigs.softer_sweetBery)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.setBlock(4, 1, 6, Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(BlockStateProperties.AGE_3, 3));
        helper.setBlock(4, 1, 2, Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(BlockStateProperties.AGE_3, 3));

        Player player_sneak = helper.makeMockPlayer(GameType.SURVIVAL);
        Player player_armored = helper.makeMockPlayer(GameType.SURVIVAL);

        helper.startSequence()
                .thenExecute(() -> {
                    player_sneak.setPos(new BlockPos(4, 1, 6).getCenter());

                    player_sneak.setPose(Pose.CROUCHING);
                    player_sneak.setShiftKeyDown(true);
                    //helper.walkTo(player_armored, new BlockPos(4, 1, 6), 1);
                })
                .thenExecute(() -> {
                    player_sneak.move(MoverType.PLAYER, new BlockPos(1, 1, 6).getCenter());
                })
                .thenExecuteAfter(20 * 2, () -> {
                    System.out.println(player_sneak.getHealth());
                })
                .thenSucceed();
    }
}
