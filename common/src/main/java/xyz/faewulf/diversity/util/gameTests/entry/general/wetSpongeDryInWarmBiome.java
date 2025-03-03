package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.DEFAULT;

@TestGroup
public class wetSpongeDryInWarmBiome {
    @GameTest(template = DEFAULT)
    public void test(GameTestHelper helper) {

        if (!ModConfigs.wet_sponge_dry_in_warm_biome)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        helper.setBiome(Biomes.DESERT);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(Items.WET_SPONGE, 64);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.placeAt(player, itemStack, new BlockPos(4, 0, 4), Direction.UP);
                })
                .thenExecuteAfter(20, () -> {
                    helper.assertEntitiesPresent(EntityType.TEXT_DISPLAY, 1);
                })
                .thenSucceed();
    }
}
