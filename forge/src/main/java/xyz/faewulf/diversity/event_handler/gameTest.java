package xyz.faewulf.diversity.event_handler;

import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.gameTests.registerGameTests;


@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class gameTest {
    @SubscribeEvent
    public static void onGameTest(RegisterGameTestsEvent registerGameTestsEvent) {
        registerGameTestsEvent.register(registerGameTests.class);
    }
}
