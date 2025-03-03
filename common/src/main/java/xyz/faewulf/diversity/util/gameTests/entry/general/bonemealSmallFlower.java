package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.DEFAULT;

@TestGroup
public class bonemealSmallFlower {
    @GameTest(template = DEFAULT)
    public void test_normal(GameTestHelper helper) {

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);

        BlockPos dispenserPos = new BlockPos(4, 2, 5);
        helper.setBlock(4, 2, 4, Blocks.LILY_OF_THE_VALLEY);
        helper.setBlock(4, 1, 4, Blocks.MYCELIUM);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = helper.getBlockEntity(dispenserPos);

        dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {

                    for (int x = 0; x < 9; x++) {
                        for (int z = 0; z < 9; z++) {
                            helper.setBlock(x, 0, z, Blocks.AIR);
                            helper.setBlock(x, 0, z, Blocks.MYCELIUM);
                        }
                    }

                    helper.setBlock(4, 1, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 1, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertItemEntityPresent(Items.LILY_OF_THE_VALLEY);

                    if (dispenserBlockEntity.getItem(0).getCount() == 64) {
                        helper.fail("Bonemeal amount not change.");
                    }
                })
                .thenSucceed();
    }

    @GameTest(template = DEFAULT)
    public void test_not_spread(GameTestHelper helper) {

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);

        BlockPos dispenserPos = new BlockPos(4, 2, 5);
        helper.setBlock(4, 2, 4, Blocks.LILY_OF_THE_VALLEY);
        helper.setBlock(4, 1, 4, Blocks.MYCELIUM);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = helper.getBlockEntity(dispenserPos);

        dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {

                    for (int x = 0; x < 9; x++) {
                        for (int z = 0; z < 9; z++) {
                            helper.setBlock(x, 0, z, Blocks.AIR);
                            helper.setBlock(x, 0, z, Blocks.GRASS_BLOCK);
                        }
                    }

                    helper.setBlock(4, 1, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 1, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertItemEntityNotPresent(Items.LILY_OF_THE_VALLEY);

                    if (dispenserBlockEntity.getItem(0).getCount() == 64) {
                        helper.fail("Bonemeal amount not change.");
                    }
                })
                .thenSucceed();
    }

    @GameTest(template = DEFAULT)
    public void test_not_mycelium(GameTestHelper helper) {

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);

        BlockPos dispenserPos = new BlockPos(4, 2, 5);
        helper.setBlock(4, 2, 4, Blocks.LILY_OF_THE_VALLEY);
        helper.setBlock(4, 1, 4, Blocks.GRASS_BLOCK);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = helper.getBlockEntity(dispenserPos);

        dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {

                    for (int x = 0; x < 9; x++) {
                        for (int z = 0; z < 9; z++) {
                            helper.setBlock(x, 0, z, Blocks.AIR);
                            helper.setBlock(x, 0, z, Blocks.MYCELIUM);
                        }
                    }

                    helper.setBlock(4, 1, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 1, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertItemEntityNotPresent(Items.LILY_OF_THE_VALLEY);

                    if (dispenserBlockEntity.getItem(0).getCount() != 64) {
                        helper.fail("Bonemeal amount changed.");
                    }
                })
                .thenSucceed();
    }

    @GameTest(template = DEFAULT)
    public void test_blacklist(GameTestHelper helper) {

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);

        BlockPos dispenserPos = new BlockPos(4, 2, 5);
        helper.setBlock(4, 2, 4, Blocks.WITHER_ROSE);
        helper.setBlock(4, 1, 4, Blocks.MYCELIUM);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = helper.getBlockEntity(dispenserPos);

        dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {

                    for (int x = 0; x < 9; x++) {
                        for (int z = 0; z < 9; z++) {
                            helper.setBlock(x, 0, z, Blocks.AIR);
                            helper.setBlock(x, 0, z, Blocks.MYCELIUM);
                        }
                    }

                    helper.setBlock(4, 1, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 1, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertItemEntityNotPresent(Items.WITHER_ROSE);

                    if (dispenserBlockEntity.getItem(0).getCount() != 64) {
                        helper.fail("Bonemeal amount changed.");
                    }
                })
                .thenSucceed();
    }
}
