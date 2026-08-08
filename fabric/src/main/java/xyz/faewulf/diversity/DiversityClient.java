package xyz.faewulf.diversity;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.faewulf.diversity.event_handler.blockDayCounterMessage;

@Environment(EnvType.CLIENT)
public class DiversityClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        blockDayCounterMessage.register();
    }
}
