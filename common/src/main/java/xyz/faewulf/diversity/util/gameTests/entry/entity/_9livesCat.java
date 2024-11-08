package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class _9livesCat {

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 600)
    public void test(GameTestHelper helper) {

        if (!ModConfigs._9_lives_cat)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer();
        Cat cat = helper.spawn(EntityType.CAT, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.DIAMOND_AXE, 1);
        itemStack.enchant(Enchantments.SHARPNESS, 10);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> player.attack(cat))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.fail("Cat died :(. Turn = 1");
                })
                .thenExecute(() -> player.attack(cat))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.fail("Cat died :(. Turn = 2");
                })
                .thenExecute(() -> player.attack(cat))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.fail("Cat died :(. Turn = 3");
                })
                .thenExecute(() -> player.attack(cat))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.fail("Cat died :(. Turn = 4");
                })
                .thenExecute(() -> player.attack(cat))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.fail("Cat died :(. Turn = 5");
                })
                .thenExecute(() -> player.attack(cat))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.fail("Cat died :(. Turn = 6");
                })
                .thenExecute(() -> player.attack(cat))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.fail("Cat died :(. Turn = 7");
                })
                .thenExecute(() -> player.attack(cat))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.fail("Cat died :(. Turn = 8");
                })
                .thenExecute(() -> {
                    if (helper.getEntities(EntityType.CAT, new BlockPos(4, 4, 4), 5).isEmpty())
                        helper.succeed();
                    else
                        helper.fail("Cat still alive!");
                });


    }
}
