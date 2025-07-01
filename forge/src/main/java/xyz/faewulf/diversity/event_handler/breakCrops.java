package xyz.faewulf.diversity.event_handler;


import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.event.xpCrops;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class breakCrops {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        xpCrops.run(event.getPlayer().level(), event.getPlayer(), event.getPos(), event.getState());
    }
}
