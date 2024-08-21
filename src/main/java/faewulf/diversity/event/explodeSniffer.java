package faewulf.diversity.event;

import faewulf.diversity.inter.ICustomSniffer;
import faewulf.diversity.inter.typeSnort;
import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class explodeSniffer {

    private static final List<Item> acceptItems = new ArrayList<>() {{
        add(Items.SUGAR);
        add(Items.BLAZE_POWDER);
        add(Items.GUNPOWDER);
        add(Items.REDSTONE);
        add(Items.GLOWSTONE_DUST);
        add(Items.BONE_MEAL);
    }};

    public static void run() {


        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {

            //if not enable in config file
            if (!ModConfigs.explosive_sniffer)
                return ActionResult.PASS;

            //if not mainhand
            if ((entity.getType() == EntityType.SNIFFER)
                    && hand == Hand.MAIN_HAND
                    && acceptItems.contains(player.getStackInHand(hand).getItem())
                    && hitResult == null
            ) {
                if (!world.isClient) {


                    //if already snort
                    if (((ICustomSniffer) entity).getSnortType() != null)
                        return ActionResult.PASS;

                    Item item = player.getStackInHand(hand).getItem();

                    if (item == Items.SUGAR)
                        ((ICustomSniffer) entity).setSnortType(typeSnort.SUGAR);
                    else if (item == Items.GLOWSTONE_DUST)
                        ((ICustomSniffer) entity).setSnortType(typeSnort.GLOW_DUST);
                    else if (item == Items.REDSTONE)
                        ((ICustomSniffer) entity).setSnortType(typeSnort.REDSTONE);
                    else if (item == Items.BLAZE_POWDER)
                        ((ICustomSniffer) entity).setSnortType(typeSnort.BLAZE_POWDER);
                    else if (item == Items.BONE_MEAL)
                        ((ICustomSniffer) entity).setSnortType(typeSnort.BONE_MEAL);
                    else if (item == Items.GUNPOWDER)
                        ((ICustomSniffer) entity).setSnortType(typeSnort.GUN_POWDER);

                    if (!player.isCreative()) {
                        player.getStackInHand(hand).decrement(1);
                    }

                    entity.playSound(SoundEvents.ITEM_BONE_MEAL_USE, 1.0f, 1.0f);
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        }));
    }
}
