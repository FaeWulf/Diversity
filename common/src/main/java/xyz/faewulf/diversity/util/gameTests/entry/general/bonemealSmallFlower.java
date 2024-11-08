package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class bonemealSmallFlower {
    @GameTest(template = registerGameTests.DEFAULT)
    public void test_normal(GameTestHelper helper) {

        if (!ModConfigs.bonemeal_small_flower) helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);

        BlockPos dispenserPos = new BlockPos(4, 3, 5);
        helper.setBlock(4, 3, 4, Blocks.LILY_OF_THE_VALLEY);
        helper.setBlock(4, 2, 4, Blocks.MYCELIUM);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity) helper.getBlockEntity(dispenserPos);

        if (dispenserBlockEntity != null)
            dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {

                    for (int x = 0; x < 9; x++) {
                        for (int z = 0; z < 9; z++) {
                            helper.setBlock(x, 1, z, Blocks.AIR);
                            helper.setBlock(x, 1, z, Blocks.MYCELIUM);
                        }
                    }

                    helper.setBlock(4, 2, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 2, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertItemEntityPresent(Items.LILY_OF_THE_VALLEY, new BlockPos(4, 4, 4), 5);

                    if (dispenserBlockEntity != null)
                        if (dispenserBlockEntity.getItem(0).getCount() == 64) {
                            helper.fail("Bonemeal amount not change.");
                        }
                })
                .thenSucceed();
    }

    @GameTest(template = registerGameTests.DEFAULT)
    public void test_not_spread(GameTestHelper helper) {

        if (!ModConfigs.bonemeal_small_flower) helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);

        BlockPos dispenserPos = new BlockPos(4, 3, 5);
        helper.setBlock(4, 3, 4, Blocks.LILY_OF_THE_VALLEY);
        helper.setBlock(4, 2, 4, Blocks.MYCELIUM);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity) helper.getBlockEntity(dispenserPos);

        if (dispenserBlockEntity != null)
            dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {

                    for (int x = 0; x < 9; x++) {
                        for (int z = 0; z < 9; z++) {
                            helper.setBlock(x, 1, z, Blocks.AIR);
                            helper.setBlock(x, 1, z, Blocks.GRASS_BLOCK);
                        }
                    }

                    helper.setBlock(4, 2, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 2, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertItemEntityPresent(Items.LILY_OF_THE_VALLEY, new BlockPos(4, 4, 4), 5);

                    if (dispenserBlockEntity != null)
                        if (dispenserBlockEntity.getItem(0).getCount() == 64) {
                            helper.fail("Bonemeal amount not change.");
                        }
                })
                .thenSucceed();
    }

    @GameTest(template = registerGameTests.DEFAULT)
    public void test_not_mycelium(GameTestHelper helper) {

        if (!ModConfigs.bonemeal_small_flower) helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);

        BlockPos dispenserPos = new BlockPos(4, 3, 5);
        helper.setBlock(4, 3, 4, Blocks.LILY_OF_THE_VALLEY);
        helper.setBlock(4, 2, 4, Blocks.GRASS_BLOCK);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity) helper.getBlockEntity(dispenserPos);

        if (dispenserBlockEntity != null)
            dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {

                    for (int x = 0; x < 9; x++) {
                        for (int z = 0; z < 9; z++) {
                            helper.setBlock(x, 1, z, Blocks.AIR);
                            helper.setBlock(x, 1, z, Blocks.MYCELIUM);
                        }
                    }

                    helper.setBlock(4, 2, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 2, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertItemEntityPresent(Items.LILY_OF_THE_VALLEY, new BlockPos(4, 4, 4), 5);

                    if (dispenserBlockEntity != null)
                        if (dispenserBlockEntity.getItem(0).getCount() != 64) {
                            helper.fail("Bonemeal amount changed.");
                        }
                })
                .thenSucceed();
    }

    @GameTest(template = registerGameTests.DEFAULT)
    public void test_blacklist(GameTestHelper helper) {

        if (!ModConfigs.bonemeal_small_flower) helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);

        BlockPos dispenserPos = new BlockPos(4, 3, 5);
        helper.setBlock(4, 3, 4, Blocks.WITHER_ROSE);
        helper.setBlock(4, 2, 4, Blocks.MYCELIUM);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity) helper.getBlockEntity(dispenserPos);

        if (dispenserBlockEntity != null)
            dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {

                    for (int x = 0; x < 9; x++) {
                        for (int z = 0; z < 9; z++) {
                            helper.setBlock(x, 1, z, Blocks.AIR);
                            helper.setBlock(x, 1, z, Blocks.MYCELIUM);
                        }
                    }

                    helper.setBlock(4, 2, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 2, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertItemEntityPresent(Items.WITHER_ROSE, new BlockPos(4, 4, 4), 5);

                    if (dispenserBlockEntity != null)
                        if (dispenserBlockEntity.getItem(0).getCount() != 64) {
                            helper.fail("Bonemeal amount changed.");
                        }
                })
                .thenSucceed();
    }
}
