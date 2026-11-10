package xyz.faewulf.diversity.event_handler;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.event.xpCrops;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class breakCrops {
    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        xpCrops.run(event.getPlayer().level(), event.getPlayer(), event.getPos(), event.getState());
    }
}
