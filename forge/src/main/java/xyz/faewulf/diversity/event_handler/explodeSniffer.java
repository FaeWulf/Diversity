package xyz.faewulf.diversity.event_handler;

import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import xyz.faewulf.diversity.Constants;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class explodeSniffer {
    @SubscribeEvent
    public static boolean onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        InteractionResult interactionResult = xyz.faewulf.diversity.event.explodeSniffer.run(event.getLevel(), event.getEntity(), event.getHand(), event.getTarget(), null);
        return interactionResult.consumesAction();
    }
}
