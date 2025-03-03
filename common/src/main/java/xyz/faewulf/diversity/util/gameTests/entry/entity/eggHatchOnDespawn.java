package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.mixin.core.invoker.ItemEntityInvoker;
import xyz.faewulf.diversity.util.config.ModConfigs;

import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.DEFAULT;

@TestGroup
public class eggHatchOnDespawn {

    @GameTest(template = DEFAULT)
    public void test_onHayBale(GameTestHelper helper) {

        if (!ModConfigs.chicken_egg_despawn_tryhatch)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.setBlock(4, 1, 4, Blocks.HAY_BLOCK);

        ItemEntity items = helper.spawn(EntityType.ITEM, 4, 2, 4);
        items.setItem(new ItemStack(Items.EGG, 64));

        helper.startSequence()
                .thenExecute(() -> ((ItemEntityInvoker) items).setAge(6000))
                .thenExecuteAfter(20 * 2, () -> {
                    helper.assertEntityPresent(EntityType.CHICKEN);
                })
                .thenSucceed();
    }


    @GameTest(template = DEFAULT)
    public void test_onNormalBlock(GameTestHelper helper) {

        if (!ModConfigs.chicken_egg_despawn_tryhatch)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        ItemEntity items = helper.spawn(EntityType.ITEM, 4, 2, 4);
        items.setItem(new ItemStack(Items.EGG, 64));

        helper.startSequence()
                .thenExecute(() -> ((ItemEntityInvoker) items).setAge(6000))
                .thenExecuteAfter(20 * 2, () -> {
                    helper.assertEntityNotPresent(EntityType.CHICKEN);
                })
                .thenSucceed();
    }
}
