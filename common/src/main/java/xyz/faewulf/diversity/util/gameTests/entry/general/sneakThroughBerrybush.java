package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

@TestGroup
public class sneakThroughBerrybush {


    public void test(GameTestHelper helper) {
        if (!ModConfigs.softer_sweetBery)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        helper.setBlock(4, 1, 6, Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(BlockStateProperties.AGE_3, 3));
        helper.setBlock(4, 1, 2, Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(BlockStateProperties.AGE_3, 3));

        Player player_sneak = helper.makeMockPlayer(GameType.SURVIVAL);
        Player player_armored = helper.makeMockPlayer(GameType.SURVIVAL);

        helper.startSequence()
                .thenExecute(() -> {
                    player_sneak.setPos(Vec3.atCenterOf(new BlockPos(4, 1, 6)));

                    player_sneak.setPose(Pose.CROUCHING);
                    player_sneak.setShiftKeyDown(true);
                    //helper.walkTo(player_armored, new BlockPos(4, 1, 6), 1);
                })
                .thenExecute(() -> {
                    player_sneak.move(MoverType.PLAYER, Vec3.atCenterOf(new BlockPos(1, 1, 6)));
                })
                .thenExecuteAfter(20 * 2, () -> {
                    System.out.println(player_sneak.getHealth());
                })
                .thenSucceed();
    }
}
