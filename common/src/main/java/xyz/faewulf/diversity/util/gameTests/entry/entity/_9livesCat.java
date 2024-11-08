package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.converter;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class _9livesCat {

    @GameTest(template = registerGameTests.DEFAULT, timeoutTicks = 600)
    public void test(GameTestHelper helper) {

        if (!ModConfigs._9_lives_cat)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Cat cat = helper.spawn(EntityType.CAT, new BlockPos(4, 2, 4).getCenter());

        ItemStack itemStack = new ItemStack(Items.DIAMOND_AXE, 1);
        itemStack.enchant(converter.getEnchant(helper.getLevel(), Enchantments.SHARPNESS), 5);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.fail("Cat died :(. Turn = 1");
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.fail("Cat died :(. Turn = 2");
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.fail("Cat died :(. Turn = 3");
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.fail("Cat died :(. Turn = 4");
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.fail("Cat died :(. Turn = 5");
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.fail("Cat died :(. Turn = 6");
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.fail("Cat died :(. Turn = 7");
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.fail("Cat died :(. Turn = 8");
                })
                .thenExecute(() -> {
                    if (helper.getEntities(EntityType.CAT).isEmpty())
                        helper.succeed();
                    else
                        helper.fail("Cat still alive!");
                });


    }
}
