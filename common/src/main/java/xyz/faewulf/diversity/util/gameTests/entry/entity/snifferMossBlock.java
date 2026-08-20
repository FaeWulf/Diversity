package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import java.util.List;

@TestGroup
public class snifferMossBlock {

    //@GameTest(template = DEFAULT, timeoutTicks = 1200)
    public void test_bonemeal(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                helper.setBlock(x, 0, z, Blocks.MOSS_BLOCK.defaultBlockState());
            }
        }

        Sniffer sniffer = helper.spawn(EntityTypes.SNIFFER, Vec3.atCenterOf(new BlockPos(4, 2, 4)));
        sniffer.transitionTo(Sniffer.State.SCENTING);

        helper.startSequence()
                .thenExecuteAfter(20 * 20, () -> {
                    List<ItemEntity> itemEntities = helper.getEntities(EntityTypes.ITEM);

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
