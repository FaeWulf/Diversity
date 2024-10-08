package xyz.faewulf.diversity.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import xyz.faewulf.diversity.inter.ICustomBrushableBlockEntity;
import xyz.faewulf.diversity.util.MissingMethod.ItemStackMethod;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.ArrayList;
import java.util.List;

public class putItemIntoBrushableBlocks {

    private static final List<Item> blackListItems = new ArrayList<>() {{
        //this.add(Items.SHULKER_BOX);
        //this.add(Items.BUNDLE);
    }};

    public static InteractionResult run(Level world, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!ModConfigs.usable_suspicious_block)
            return InteractionResult.PASS;

        //first filter
        if (hand.equals(InteractionHand.MAIN_HAND)
                && player.isShiftKeyDown()
                && hitResult.getType().equals(HitResult.Type.BLOCK)
        ) {

            BlockEntity checkBlock = world.getBlockEntity(hitResult.getBlockPos());

            if (checkBlock == null)
                return InteractionResult.PASS;

            if (checkBlock instanceof BrushableBlockEntity brushableBlockEntity) {


                ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);

                //filter blacklist item
                if (mainHand.isEmpty()
                        || blackListItems.contains(mainHand.getItem())
                )
                    return InteractionResult.PASS;

                //if alrady has item in it
                if (!brushableBlockEntity.getItem().isEmpty() || brushableBlockEntity.getItem().getItem() != Items.AIR)
                    return InteractionResult.PASS;


                ItemStack treasure = mainHand.copyWithCount(1);

                ((ICustomBrushableBlockEntity) brushableBlockEntity).setItem(treasure);

                //mainHand.consume(1, player);
                player.swing(hand, true);
                ItemStackMethod.consume(mainHand, 1, player);

                world.playSound(null, player.blockPosition(), SoundEvents.SAND_PLACE, SoundSource.PLAYERS, 1.0f, 1.0f);

                return InteractionResult.SUCCESS;
            }


        }

        return InteractionResult.PASS;
    }
}
