package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.UNDERWATER;

@TestGroup
public class bonemealCoral {
    @GameTest(template = UNDERWATER)
    public void test_normal(GameTestHelper helper) {

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);
        BlockPos coralPos = new BlockPos(4, 2, 4);
        BlockPos dispenserPos = new BlockPos(4, 2, 5);

        helper.setBiome(Biomes.WARM_OCEAN);
        helper.setBlock(4, 1, 4, Blocks.BUBBLE_CORAL_BLOCK);
        helper.setBlock(coralPos, Blocks.BUBBLE_CORAL_FAN);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = helper.getBlockEntity(dispenserPos);

        dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {
                    helper.setBlock(4, 1, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 1, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertBlockNotPresent(Blocks.BUBBLE_CORAL_FAN, coralPos);
                    if (dispenserBlockEntity.getItem(0).getCount() == 64) {
                        helper.fail("Bonemeal amount not change.");
                    }
                })
                .thenSucceed();
    }

    @GameTest(template = UNDERWATER)
    public void test_not_trigger(GameTestHelper helper) {

        ItemStack bonemeal = new ItemStack(Items.BONE_MEAL, 64);
        BlockPos coralPos = new BlockPos(4, 2, 4);
        BlockPos dispenserPos = new BlockPos(4, 2, 5);

        helper.setBlock(4, 1, 4, Blocks.BUBBLE_CORAL_BLOCK);
        helper.setBlock(coralPos, Blocks.BUBBLE_CORAL_FAN);

        helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState());
        DispenserBlockEntity dispenserBlockEntity = helper.getBlockEntity(dispenserPos);

        dispenserBlockEntity.setItem(0, bonemeal);

        helper.startSequence()
                .thenExecuteFor(20 * 5, () -> {
                    helper.setBlock(4, 1, 5, Blocks.REDSTONE_BLOCK);
                    helper.setBlock(4, 1, 5, Blocks.AIR);
                })
                .thenExecute(() -> {
                    helper.assertBlockPresent(Blocks.BUBBLE_CORAL_FAN, coralPos);

                    if (dispenserBlockEntity.getItem(0).getCount() != 64) {
                        helper.fail("Bonemeal amount changed.");
                    }
                })
                .thenSucceed();
    }
}
