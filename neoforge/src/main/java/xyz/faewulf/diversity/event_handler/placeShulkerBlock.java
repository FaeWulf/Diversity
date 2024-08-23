package xyz.faewulf.diversity.event_handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import xyz.faewulf.diversity.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class placeShulkerBlock {
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem(); // Item in the main hand
            if (itemStack.isEmpty()) {
                itemStack = player.getOffhandItem(); // Fallback to the offhand if main hand is empty
            }
            xyz.faewulf.diversity.event.placeShulkerBlock.run(player.level(), player, itemStack, event.getPos());
        }
    }
}
