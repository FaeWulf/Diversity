package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

@TestGroup
public class brushableParrotChicken {


    public void test_parrot(GameTestHelper helper) {

        if (!ModConfigs.brushable_parrot_chicken)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Parrot parrot = helper.spawn(EntityType.PARROT, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.BRUSH, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setPose(Pose.CROUCHING);
                    player.interactOn(parrot, InteractionHand.MAIN_HAND);
                })
                .thenExecuteAfter(20 * 2, () -> {
                    helper.assertItemEntityPresent(Items.FEATHER);

                    if (itemStack.getDamageValue() == 0)
                        helper.fail(Component.literal("Shear not get damage."));
                })
                .thenSucceed();
    }


    public void test_chicken(GameTestHelper helper) {

        if (!ModConfigs.brushable_parrot_chicken)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Chicken chicken = helper.spawn(EntityType.CHICKEN, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.SHEARS, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setShiftKeyDown(true);
                    player.interactOn(chicken, InteractionHand.MAIN_HAND);
                })
                .thenExecuteAfter(20 * 2, () -> {
                    helper.assertItemEntityPresent(Items.FEATHER);

                    if (itemStack.getDamageValue() == 0)
                        helper.fail(Component.literal("Shear not get damage."));
                })
                .thenSucceed();
    }
}
