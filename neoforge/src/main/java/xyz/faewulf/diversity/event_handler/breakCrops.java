package xyz.faewulf.diversity.event_handler;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import xyz.faewulf.diversity.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class breakCrops {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        xyz.faewulf.diversity.event.breakCrops.run(event.getPlayer().level(), event.getPlayer(), event.getPos(), event.getState());
    }
}
