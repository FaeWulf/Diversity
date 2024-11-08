package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class glowBerryEffect {

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 200)
    public void test(GameTestHelper helper) {

        if (!ModConfigs.glow_berry_glowing)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Villager fox = helper.spawn(EntityType.VILLAGER, 4, 2, 4);

        ItemStack itemStack = new ItemStack(Items.GLOW_BERRIES, 1);
        fox.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> fox.startUsingItem(InteractionHand.MAIN_HAND))
                .thenExecuteAfter(20 * 3, () -> {
                            if (!fox.hasEffect(MobEffects.GLOWING))
                                helper.fail("Doesn't have glow effect");
                        }
                )
                .thenSucceed();
    }
}
