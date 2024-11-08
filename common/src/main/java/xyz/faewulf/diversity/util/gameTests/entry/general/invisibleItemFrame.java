package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

import java.util.List;

@TestGroup
public class invisibleItemFrame {
    @GameTest(template = registerGameTests.DEFAULT)
    public void test(GameTestHelper helper) {

        if (!ModConfigs.invisible_frame)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.setBlock(4, 2, 3, Blocks.GLASS);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemFrame itemFrame = helper.spawn(EntityType.ITEM_FRAME, 4, 2, 4);

        ItemStack itemStack = new ItemStack(Items.GLASS_PANE, 2);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setShiftKeyDown(true);
                    player.setPose(Pose.CROUCHING);
                })
                //1st test; trigger
                .thenExecute(() -> player.interactOn(itemFrame, InteractionHand.MAIN_HAND))
                .thenExecute(() -> {
                    if (itemFrame.getItem().getItem() != Items.AIR)
                        helper.fail("Not trigger set visible mode.");
                })
                .thenExecute(() -> {
                    player.setShiftKeyDown(false);
                    player.setPose(Pose.STANDING);
                })
                //2nd test: invisible after insert item
                .thenExecute(() -> player.interactOn(itemFrame, InteractionHand.MAIN_HAND))
                .thenExecute(() -> {
                    if (!itemFrame.isInvisible())
                        helper.fail("Insert item not make ItemFrame invisible");
                })
                //3rd test: drop glass after break
                .thenExecute(() -> {
                    player.attack(itemFrame);
                    player.attack(itemFrame);
                })
                .thenExecuteAfter(20 * 2, () -> {
                    List<ItemEntity> itemEntities = helper.getEntities(EntityType.ITEM);

                    boolean fail = true;

                    for (ItemEntity itemEntity : itemEntities) {
                        if (itemEntity.getItem().getItem() == Items.GLASS_PANE && itemEntity.getItem().getCount() == 2)
                            fail = false;
                    }

                    if (fail)
                        helper.fail(itemEntities.toString());

                })
                .thenSucceed();
    }
}
