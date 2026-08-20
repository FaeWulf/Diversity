package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.EnchantHelper;
import xyz.faewulf.lib.util.gameTests.TestGroup;

@TestGroup
public class _9livesCat {

    //@GameTest(template = DEFAULT, timeoutTicks = 600)
    public void test(GameTestHelper helper) {

        if (!ModConfigs._9_lives_cat)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Cat cat = helper.spawn(EntityTypes.CAT, Vec3.atCenterOf(new BlockPos(4, 2, 4)));


        ItemStack itemStack = new ItemStack(Items.DIAMOND_AXE, 1);
        itemStack.enchant(EnchantHelper.getEnchant(helper.getLevel(), Enchantments.SHARPNESS), 5);

        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.fail(Component.literal("Cat died :(. Turn = 1"));
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.fail(Component.literal("Cat died :(. Turn = 2"));
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.fail(Component.literal("Cat died :(. Turn = 3"));
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.fail(Component.literal("Cat died :(. Turn = 4"));
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.fail(Component.literal("Cat died :(. Turn = 5"));
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.fail(Component.literal("Cat died :(. Turn = 6"));
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.fail(Component.literal("Cat died :(. Turn = 7"));
                })
                .thenExecute(() -> helper.hurt(cat, player.damageSources().playerAttack(player), 40))
                .thenExecuteAfter(20 * 2, () -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.fail(Component.literal("Cat died :(. Turn = 8"));
                })
                .thenExecute(() -> {
                    if (helper.getEntities(EntityTypes.CAT).isEmpty())
                        helper.succeed();
                    else
                        helper.fail(Component.literal("Cat still alive!"));
                });


    }
}
