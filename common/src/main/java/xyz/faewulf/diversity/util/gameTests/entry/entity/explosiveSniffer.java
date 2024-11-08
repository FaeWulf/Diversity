package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class explosiveSniffer {
    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 600)
    public void test_gunpowder(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.GUNPOWDER, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setPose(Pose.CROUCHING);
                    player.interactOn(sniffer, InteractionHand.MAIN_HAND);
                })
                .thenExecuteAfter(20 * 15, () -> {
                    if (itemStack.getCount() > 0) helper.fail("Item not consumed");

                    if (helper.getEntities(EntityType.SNIFFER).isEmpty() || sniffer.getHealth() < sniffer.getMaxHealth()) {
                        helper.succeed();
                    } else
                        helper.fail("Gunpowder case not trigger, or sniffer doesn't explode. " + sniffer.getHealth());
                })
                .thenSucceed();
    }

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 600)
    public void test_blaze_powder(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.BLAZE_POWDER, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setPose(Pose.CROUCHING);
                    player.interactOn(sniffer, InteractionHand.MAIN_HAND);
                })
                .thenExecuteAfter(20 * 15, () -> {
                    if (itemStack.getCount() > 0) helper.fail("Item not consumed");

                    if (helper.getEntities(EntityType.SNIFFER).isEmpty() || sniffer.getHealth() < sniffer.getMaxHealth()) {
                        helper.succeed();
                    } else
                        helper.fail("Blaze powder case not trigger, or sniffer doesn't explode. " + sniffer.getHealth());
                })
                .thenSucceed();
    }

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 600)
    public void test_glowstone_dust(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.GLOWSTONE_DUST, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setPose(Pose.CROUCHING);
                    player.interactOn(sniffer, InteractionHand.MAIN_HAND);
                })
                .thenExecuteAfter(20 * 15, () -> {
                    if (itemStack.getCount() > 0) helper.fail("Item not consumed");

                    if (helper.getEntities(EntityType.SNIFFER).isEmpty() || sniffer.getHealth() < sniffer.getMaxHealth()) {
                        helper.succeed();
                    } else
                        helper.fail("Glowstone dust case not trigger, or sniffer doesn't explode. " + sniffer.getHealth());
                })
                .thenSucceed();
    }

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 600)
    public void test_redstone_dust(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.REDSTONE, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setPose(Pose.CROUCHING);
                    player.interactOn(sniffer, InteractionHand.MAIN_HAND);
                })
                .thenExecuteAfter(20 * 15, () -> {
                    if (itemStack.getCount() > 0) helper.fail("Item not consumed");

                    if (helper.getEntities(EntityType.SNIFFER).isEmpty() || sniffer.getHealth() < sniffer.getMaxHealth()) {
                        helper.succeed();
                    } else
                        helper.fail("Redstone dust case not trigger, or sniffer doesn't explode. " + sniffer.getHealth());
                })
                .thenSucceed();
    }

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 600)
    public void test_sugar(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.SUGAR, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setPose(Pose.CROUCHING);
                    player.interactOn(sniffer, InteractionHand.MAIN_HAND);
                })
                .thenExecuteAfter(20 * 15, () -> {
                    if (itemStack.getCount() > 0) helper.fail("Item not consumed");

                    if (helper.getEntities(EntityType.SNIFFER).isEmpty() || sniffer.getHealth() < sniffer.getMaxHealth()) {
                        helper.succeed();
                    } else
                        helper.fail("Sugar case not trigger, or sniffer doesn't explode. " + sniffer.getHealth());
                })
                .thenSucceed();
    }

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 600)
    public void test_bonemeal(GameTestHelper helper) {

        if (!ModConfigs.explosive_sniffer)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                helper.setBlock(x, 0, z, Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.BONE_MEAL, 1);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setPose(Pose.CROUCHING);
                    player.interactOn(sniffer, InteractionHand.MAIN_HAND);
                })
                .thenExecuteAfter(20 * 15, () -> {
                    if (itemStack.getCount() > 0) helper.fail("Item not consumed");

                    if (helper.getEntities(EntityType.SNIFFER).isEmpty() || sniffer.getHealth() < sniffer.getMaxHealth()) {
                        helper.succeed();
                    } else
                        helper.fail("Bonemeal not trigger, or sniffer doesn't explode. " + sniffer.getHealth());
                })
                .thenSucceed();
    }
}
