package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

import java.util.List;

@TestGroup
public class snifferMossBlock {

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 1200)
    public void test_bonemeal(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                helper.setBlock(x, 1, z, Blocks.MOSS_BLOCK.defaultBlockState());
            }
        }

        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, new BlockPos(4, 3, 4).getCenter());
        sniffer.transitionTo(Sniffer.State.SCENTING);

        helper.startSequence()
                .thenExecuteAfter(20 * 20, () -> {
                    List<ItemEntity> itemEntities = helper.getEntities(EntityType.ITEM, new BlockPos(4, 4, 4), 5);

                    for (ItemEntity itemEntity : itemEntities) {
                        if (itemEntity.getItem().getItem() == Items.SPORE_BLOSSOM || itemEntity.getItem().getItem() == Items.SMALL_DRIPLEAF) {
                            helper.succeed();
                            return;
                        }
                    }

                    helper.fail("Not drops items: " + itemEntities);
                });
    }
}
