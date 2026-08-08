package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.core.BlockPos;
 
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.DEFAULT;

import java.util.List;

@TestGroup
public class snifferMossBlock {

    //@GameTest(template = DEFAULT, timeoutTicks = 1200)
    public void test_bonemeal(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                helper.setBlock(x, 0, z, Blocks.MOSS_BLOCK.defaultBlockState());
            }
        }

        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, new BlockPos(4, 2, 4).getCenter());
        sniffer.transitionTo(Sniffer.State.SCENTING);

        helper.startSequence()
                .thenExecuteAfter(20 * 20, () -> {
                    List<ItemEntity> itemEntities = helper.getEntities(EntityType.ITEM);

                    for (ItemEntity itemEntity : itemEntities) {
                        if (itemEntity.getItem().getItem() == Items.SPORE_BLOSSOM || itemEntity.getItem().getItem() == Items.SMALL_DRIPLEAF) {
                            helper.succeed();
                            return;
                        }
                    }

                    helper.fail(Component.literal("Not drops items: " + itemEntities));
                });
    }
}
