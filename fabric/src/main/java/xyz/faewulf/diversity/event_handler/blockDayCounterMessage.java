package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class blockDayCounterMessage {
    public static void register() {
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> xyz.faewulf.diversity.event.blockDayCounterMessage.run(message));
    }
}
