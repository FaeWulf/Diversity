package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

@TestGroup
public class clickThroughItemFrame {

    public void test_itemFrame(GameTestHelper helper) {

        if (!ModConfigs.click_through_itemframe)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        helper.setBlock(4, 1, 3, Blocks.GLASS);
        helper.setBlock(3, 1, 3, Blocks.CHEST);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemFrame itemFrame_normalBlock = helper.spawn(EntityTypes.ITEM_FRAME, 4, 1, 4);
        ItemFrame itemFrame_chest = helper.spawn(EntityTypes.ITEM_FRAME, 3, 1, 4);

        ItemStack itemStack = new ItemStack(Items.GLASS_PANE, 64);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                //1st test; normal behavior on non container block
                .thenExecute(() -> {
                    player.interactOn(itemFrame_normalBlock, InteractionHand.MAIN_HAND, itemFrame_normalBlock.position());
                    player.interactOn(itemFrame_normalBlock, InteractionHand.MAIN_HAND, itemFrame_normalBlock.position());
                })
                .thenExecute(() -> {
                    if (itemFrame_normalBlock.getRotation() == 0)
                        helper.fail(Component.literal("Normal behavior not trigger"));
                })
                //2nd test on container block
                .thenExecute(() -> {
                    player.interactOn(itemFrame_chest, InteractionHand.MAIN_HAND, itemFrame_chest.position());
                    player.interactOn(itemFrame_chest, InteractionHand.MAIN_HAND, itemFrame_chest.position());
                })
                .thenExecute(() -> {
                    if (itemFrame_chest.getRotation() > 0)
                        helper.fail(Component.literal("Click through not trigger"));
                })
                //3rd test; bypass behavior
                .thenExecute(() -> {
                    player.setShiftKeyDown(true);
                    player.setPose(Pose.CROUCHING);
                })
                .thenExecute(() -> {
                    player.interactOn(itemFrame_chest, InteractionHand.MAIN_HAND, itemFrame_chest.position());
                })
                .thenExecuteAfter(2, () -> {
                    if (itemFrame_chest.getRotation() == 0)
                        helper.fail(Component.literal("Bypass click through not trigger"));
                })
                .thenSucceed();
    }


    public void test_sign(GameTestHelper helper) {

        if (!ModConfigs.click_through_itemframe)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        helper.setBlock(4, 1, 5, Blocks.CHEST);
        helper.setBlock(4, 1, 4, Blocks.OAK_WALL_SIGN);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        ItemStack itemStack = new ItemStack(Items.GLASS_PANE, 64);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                //1st test; normal behavior on non container block
                .thenExecute(() -> {
                    helper.useBlock(new BlockPos(4, 1, 4), player);
                    BlockEntity blockEntity = helper.getBlockEntity(new BlockPos(4, 1, 5), BlockEntity.class);

                    if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {
                        if (chestBlockEntity.getOpenNess(0) == 0)
                            helper.fail(Component.literal("Normal behavior not trigger"));
                    }
                })
                .thenSucceed();
    }
}
