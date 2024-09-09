package xyz.faewulf.diversity.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import xyz.faewulf.diversity.inter.ICustomItemFrame;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class invisibleItemFrame {

    public static InteractionResult run(Level world, Player player, InteractionHand hand, Entity entity, HitResult hitResult) {

        //if not enable in config file
        if (!ModConfigs.invisible_frame)
            return InteractionResult.PASS;


        //if not item frame
        //if not mainhand
        if ((entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.GLOW_ITEM_FRAME)
                && hand == InteractionHand.MAIN_HAND
                && player.getItemInHand(hand).getItem() == Items.GLASS_PANE
                && player.isShiftKeyDown()
                && hitResult == null
        ) {

            if (!world.isClientSide) {
                //if already invisible
                ICustomItemFrame frame = (ICustomItemFrame) entity;

                if (frame.getIsInvisible())
                    return InteractionResult.PASS;

                //add tag
                frame.setIsInvisible(true);

                ItemFrame itemFrame = (ItemFrame) entity;
                itemFrame.playSound(itemFrame.getAddItemSound(), 1.0f, 1.0f);
                itemFrame.playSound(SoundEvents.GLASS_PLACE, 1.0f, 1.0f);

                //reduce item count
                player.getItemInHand(hand).consume(1, player);
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

}
