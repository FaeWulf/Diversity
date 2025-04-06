package xyz.faewulf.diversity.util.gameTests.entry.general;

import net.minecraft.core.BlockPos;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.lib.util.Compare;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.DEFAULT;

import java.util.List;

@TestGroup
public class trophyBanner {


    public void trophyBanner_Wither(GameTestHelper helper) {

        if (!ModConfigs.banner_trohpy)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        BlockPos pos = new BlockPos(4, 2, 4);
        Entity target = helper.spawnWithNoFreeWill(EntityType.WITHER, pos);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        player.attack(target);
        helper.kill(target);

        List<ItemEntity> items = helper.getEntities(EntityType.ITEM, pos, 4);

        helper.runAfterDelay(20 * 2, () -> {

            for (ItemEntity item : items) {
                if (Compare.isHasTag(item.getItem().getItem(), "minecraft:banners")) {
                    helper.succeed();
                    return;
                }
            }

            helper.fail(Component.literal("No banner drop"));
        });
    }


    public void trophyBanner_Warden(GameTestHelper helper) {

        if (!ModConfigs.banner_trohpy)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        BlockPos pos = new BlockPos(4, 2, 4);
        Entity target = helper.spawnWithNoFreeWill(EntityType.WARDEN, pos);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        player.attack(target);
        helper.kill(target);

        List<ItemEntity> items = helper.getEntities(EntityType.ITEM, pos, 4);

        helper.runAfterDelay(20 * 2, () -> {

            for (ItemEntity item : items) {
                if (Compare.isHasTag(item.getItem().getItem(), "minecraft:banners")) {
                    helper.succeed();
                    return;
                }
            }

            helper.fail(Component.literal("No banner drop"));
        });
    }


    public void trophyBanner_EnderDragon(GameTestHelper helper) {

        if (!ModConfigs.banner_trohpy)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        BlockPos pos = new BlockPos(4, 2, 4);
        Entity target = helper.spawnWithNoFreeWill(EntityType.ENDER_DRAGON, pos);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        player.attack(target);
        helper.kill(target);

        List<ItemEntity> items = helper.getEntities(EntityType.ITEM, pos, 4);

        helper.runAfterDelay(20 * 2, () -> {

            for (ItemEntity item : items) {
                if (Compare.isHasTag(item.getItem().getItem(), "minecraft:banners")) {
                    helper.succeed();
                    return;
                }
            }

            helper.fail(Component.literal("No banner drop"));
        });
    }


    public void trophyBanner_ElderGuardian(GameTestHelper helper) {

        if (!ModConfigs.banner_trohpy)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        BlockPos pos = new BlockPos(4, 2, 4);
        Entity target = helper.spawnWithNoFreeWill(EntityType.ELDER_GUARDIAN, pos);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        player.attack(target);
        helper.kill(target);

        List<ItemEntity> items = helper.getEntities(EntityType.ITEM, pos, 4);

        helper.runAfterDelay(20 * 2, () -> {

            for (ItemEntity item : items) {
                if (Compare.isHasTag(item.getItem().getItem(), "minecraft:banners")) {
                    helper.succeed();
                    return;
                }
            }

            helper.fail(Component.literal("No banner drop"));
        });
    }
}
