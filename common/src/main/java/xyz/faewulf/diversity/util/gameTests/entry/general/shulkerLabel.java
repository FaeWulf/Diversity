package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import java.util.List;

@TestGroup
public class shulkerLabel {

    //@GameTest(template = DEFAULT, timeoutTicks = 1000)
    public void test(GameTestHelper helper) {

        if (!ModConfigs.shulker_label)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        ItemStack itemStack = new ItemStack(Items.SHULKER_BOX, 2);
        itemStack.set(DataComponents.CUSTOM_NAME, Component.literal("Testing"));

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                //1st test: trigger feature
                .thenExecute(() -> {
                    helper.placeAt(player, itemStack, new BlockPos(4, 0, 4), Direction.UP);
                })
                .thenExecuteAfter(20, () -> {
                    List<Display.TextDisplay> textDisplays = helper.getEntities(EntityTypes.TEXT_DISPLAY);

                    if (textDisplays.size() != 1)
                        helper.fail(Component.literal(textDisplays.toString()));

                })
                //2nd test: break shulker box
                .thenExecute(() -> {
                    helper.setBlock(4, 1, 4, Blocks.AIR);
                })
                .thenExecuteAfter(20, () -> {
                    List<Display.TextDisplay> textDisplays = helper.getEntities(EntityTypes.TEXT_DISPLAY);

                    if (!textDisplays.isEmpty())
                        helper.fail(Component.literal("2nd test failed: break shulker not kill text entity"));
                })
                //3rd test: bypass feature
                .thenExecute(() -> {
                    player.setShiftKeyDown(true);
                    player.setPose(Pose.CROUCHING);

                    helper.placeAt(player, itemStack, new BlockPos(4, 0, 4), Direction.UP);
                })
                .thenExecuteAfter(20, () -> {
                    List<Display.TextDisplay> textDisplays = helper.getEntities(EntityTypes.TEXT_DISPLAY);

                    if (!textDisplays.isEmpty())
                        helper.fail(Component.literal("3rd test failed: Sneak place not prevent spawn text entity"));

                })
                .thenSucceed();
    }
}
