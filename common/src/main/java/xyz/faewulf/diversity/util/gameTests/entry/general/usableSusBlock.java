package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

@TestGroup
public class usableSusBlock {

    public void test(GameTestHelper helper) {

        if (!ModConfigs.usable_suspicious_block)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());


        helper.setBlock(4, 1, 4, Blocks.SUSPICIOUS_SAND);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(Items.BONE_BLOCK, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setShiftKeyDown(true);
                    player.setPose(Pose.CROUCHING);

                    helper.placeAt(player, itemStack, new BlockPos(4, 1, 4), Direction.UP);
                })
                .thenSucceed();
    }
}
