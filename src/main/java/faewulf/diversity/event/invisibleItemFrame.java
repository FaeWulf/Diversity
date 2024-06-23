package faewulf.diversity.event;

import faewulf.diversity.inter.ICustomItemFrame;
import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class invisibleItemFrame {

    public static void run() {

        //if not enable in config file

        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
            if (!ModConfigs.invisible_frame)
                return ActionResult.PASS;

            //if not item frame
            //if not mainhand
            if ((entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.GLOW_ITEM_FRAME)
                    && hand == Hand.MAIN_HAND
                    && player.getStackInHand(hand).getItem() == Items.GLASS_PANE
                    && player.isSneaking()
                    && hitResult == null
            ) {
                if (!world.isClient) {

                    //if already invisible
                    ICustomItemFrame frame = (ICustomItemFrame) entity;

                    if (frame.getIsInvisible())
                        return ActionResult.PASS;

                    //add tag
                    frame.setIsInvisible(true);

                    ItemFrameEntity itemFrame = (ItemFrameEntity) entity;
                    itemFrame.playSound(itemFrame.getAddItemSound(), 1.0f, 1.0f);
                    itemFrame.playSound(SoundEvents.BLOCK_GLASS_PLACE, 1.0f, 1.0f);

                    //reduce item count
                    player.getStackInHand(hand).decrementUnlessCreative(1, player);
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        }));
    }

}
