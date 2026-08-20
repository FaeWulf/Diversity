package xyz.faewulf.diversity.util.gameTests.entry.entity;


import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Mule;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.Raft;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.gameTests.TestGroup;

@TestGroup
public class horseSitOnBoat {


    public void test1(GameTestHelper helper) {

        if (!ModConfigs.horse_can_seat_on_boat)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        Boat boat = helper.spawn(EntityTypes.ACACIA_BOAT, 4, 1, 4);
        Horse horse = helper.spawn(EntityTypes.HORSE, 1, 2, 1);
        Camel camel = helper.spawn(EntityTypes.CAMEL, 8, 2, 8);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.moveTo(camel, 4, 1, 4);
                    helper.moveTo(horse, 4, 1, 4);
                })
                .thenExecuteAfter(20 * 2, () -> {
                    if (boat.getPassengers().size() != 2)
                        helper.fail(Component.literal(boat.getPassengers().toString()));
                }).thenSucceed();
    }


    public void test2(GameTestHelper helper) {

        if (!ModConfigs.horse_can_seat_on_boat)
            helper.setBlock(8, 8, 8, Blocks.CONCRETE.red());

        Raft boat = helper.spawn(EntityTypes.BAMBOO_RAFT, 4, 1, 4);
        Donkey donkey = helper.spawn(EntityTypes.DONKEY, 1, 2, 1);
        Mule mule = helper.spawn(EntityTypes.MULE, 8, 2, 8);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.moveTo(donkey, 4, 1, 4);
                    helper.moveTo(mule, 4, 1, 4);
                })
                .thenExecuteAfter(20 * 2, () -> {
                    if (boat.getPassengers().size() != 2)
                        helper.fail(Component.literal(boat.getPassengers().toString()));
                }).thenSucceed();
    }
}