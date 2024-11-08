package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class torchFireAttack {

    @GameTest(template = registerGameTests.DEFAULT)
    public void test(GameTestHelper helper) {

        if (!ModConfigs.torch_burn_target)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Entity cow = helper.spawn(EntityType.COW, 1, 1, 1);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(Items.TORCH);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        player.attack(cow);

        helper.runAfterDelay(20 * 2, () -> {
            if (cow.isOnFire()) {
                helper.succeed(); // Pass the test if the cow is on fire
            } else {
                helper.fail("Cow was not set on fire."); // Fail if the cow isn't on fire
            }
        });
    }
}
