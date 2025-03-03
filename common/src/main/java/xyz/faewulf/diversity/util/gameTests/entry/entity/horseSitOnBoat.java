package xyz.faewulf.diversity.util.gameTests.entry.entity;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Raft;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import static xyz.faewulf.lib.api.v1.dev.GameTestHelper.DEFAULT;

@TestGroup
public class horseSitOnBoat {

    @GameTest(template = DEFAULT)
    public void test1(GameTestHelper helper) {

        if (!ModConfigs.horse_can_seat_on_boat)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Boat boat = helper.spawn(EntityType.ACACIA_BOAT, 4, 1, 4);
        Horse horse = helper.spawn(EntityType.HORSE, 1, 2, 1);
        Camel camel = helper.spawn(EntityType.CAMEL, 8, 2, 8);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.moveTo(camel, 4, 1, 4);
                    helper.moveTo(horse, 4, 1, 4);
                })
                .thenExecuteAfter(20 * 2, () -> {
                    if (boat.getPassengers().size() != 2)
                        helper.fail(boat.getPassengers().toString());
                }).thenSucceed();
    }

    @GameTest(template = DEFAULT)
    public void test2(GameTestHelper helper) {

        if (!ModConfigs.horse_can_seat_on_boat)
            helper.setBlock(8, 8, 8, Blocks.RED_CONCRETE);

        Raft boat = helper.spawn(EntityType.BAMBOO_RAFT, 4, 1, 4);
        Donkey donkey = helper.spawn(EntityType.DONKEY, 1, 2, 1);
        Mule mule = helper.spawn(EntityType.MULE, 8, 2, 8);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.moveTo(donkey, 4, 1, 4);
                    helper.moveTo(mule, 4, 1, 4);
                })
                .thenExecuteAfter(20 * 2, () -> {
                    if (boat.getPassengers().size() != 2)
                        helper.fail(boat.getPassengers().toString());
                }).thenSucceed();
    }
}