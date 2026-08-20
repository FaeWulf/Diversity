package xyz.faewulf.diversity.util.gameTests.entry.entity;


import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.mixin.core.invoker.ItemEntityInvoker;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

@TestGroup
public class eggHatchOnDespawn {


    public void test_onHayBale(GameTestHelper helper) {

        if (!ModConfigs.chicken_egg_despawn_tryhatch)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        helper.setBlock(4, 0, 4, Blocks.HAY_BLOCK);

        ItemEntity items = helper.spawn(EntityTypes.ITEM, 4, 1, 4);
        items.setItem(new ItemStack(Items.EGG, 64));

        helper.startSequence()
                .thenExecute(() -> ((ItemEntityInvoker) items).setAge(6000))
                .thenExecuteAfter(20 * 2, () -> {
                    helper.assertEntityPresent(EntityTypes.CHICKEN);
                })
                .thenSucceed();
    }


    public void test_onNormalBlock(GameTestHelper helper) {

        if (!ModConfigs.chicken_egg_despawn_tryhatch)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        ItemEntity items = helper.spawn(EntityTypes.ITEM, 4, 1, 4);
        items.setItem(new ItemStack(Items.EGG, 64));

        helper.startSequence()
                .thenExecute(() -> ((ItemEntityInvoker) items).setAge(6000))
                .thenExecuteAfter(20 * 2, () -> {
                    helper.assertEntityNotPresent(EntityTypes.CHICKEN);
                })
                .thenSucceed();
    }
}
