package faewulf.diversity.event;

import faewulf.diversity.inter.ICustomBrushableBlockEntity;
import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayList;
import java.util.List;

public class putItemIntoBrushableBlocks {

    private static final List<Item> blackListItems = new ArrayList<>() {{
        //this.add(Items.SHULKER_BOX);
        //this.add(Items.BUNDLE);
    }};


    public static void run() {


        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {

            if (!ModConfigs.usable_suspicious_block)
                return ActionResult.PASS;

            //first filter
            if (hand.equals(Hand.MAIN_HAND)
                    && player.isSneaking()
                    && hitResult.getType().equals(HitResult.Type.BLOCK)
            ) {

                BlockEntity checkBlock = world.getBlockEntity(hitResult.getBlockPos());

                if (checkBlock == null)
                    return ActionResult.PASS;

                if (checkBlock instanceof BrushableBlockEntity brushableBlockEntity) {


                    ItemStack mainHand = player.getStackInHand(Hand.MAIN_HAND);

                    //filter blacklist item
                    if (mainHand.isEmpty()
                            || blackListItems.contains(mainHand.getItem())
                    )
                        return ActionResult.PASS;

                    //if alrady has item in it
                    if (!brushableBlockEntity.getItem().isEmpty() || brushableBlockEntity.getItem().getItem() != Items.AIR)
                        return ActionResult.PASS;


                    ItemStack treasure = mainHand.copyWithCount(1);

                    ((ICustomBrushableBlockEntity) brushableBlockEntity).setItem(treasure);

                    if (!player.isCreative()) {
                        mainHand.decrement(1);
                    }

                    world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_SAND_PLACE, SoundCategory.PLAYERS, 1.0f, 1.0f);

                    return ActionResult.SUCCESS;
                }


            }

            return ActionResult.PASS;
        }));
    }
}
