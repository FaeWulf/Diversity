package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.gameTests.TestGroup;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;

@TestGroup
public class preventFarmlandTrampling {

    @GameTest(template = registerGameTests.DEFAULT)
    public void test(GameTestHelper helper) {

        if (!ModConfigs.prevent_farmland_trampling)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.setBlock(4, 1, 6, Blocks.FARMLAND);
        helper.setBlock(4, 1, 2, Blocks.FARMLAND);
        helper.setBlock(6, 1, 4, Blocks.FARMLAND);
        helper.setBlock(4, 1, 4, Blocks.ACACIA_LEAVES.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true));

        helper.startSequence()
                .thenExecute(() -> {

                    //test for slow fall effect
                    Zombie zombie_slowfall = helper.spawnWithNoFreeWill(EntityType.HUSK, new BlockPos(4, 4, 6).getCenter());
                    zombie_slowfall.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 999));

                    //test for feather falling
                    Zombie zombie_armor = helper.spawnWithNoFreeWill(EntityType.HUSK, new BlockPos(4, 4, 2).getCenter());
                    ItemStack feather_boots = new ItemStack(Items.DIAMOND_BOOTS);
                    feather_boots.enchant(Enchantments.FALL_PROTECTION, 1);
                    zombie_armor.setItemSlot(EquipmentSlot.FEET, feather_boots);

                    //test for default behavior
                    Zombie zombie_normal = helper.spawnWithNoFreeWill(EntityType.HUSK, new BlockPos(6, 4, 4).getCenter());

                })
                .thenExecuteAfter(20 * 3, () -> {
                    helper.assertBlockPresent(Blocks.FARMLAND, new BlockPos(4, 1, 6));
                    helper.assertBlockPresent(Blocks.FARMLAND, new BlockPos(4, 1, 2));
                    helper.assertBlockPresent(Blocks.DIRT, new BlockPos(6, 1, 4));
                })
                .thenSucceed();
    }
}
