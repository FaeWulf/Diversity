package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class playerChatEmote {
    public static void register() {
        ServerMessageEvents.CHAT_MESSAGE.register((xyz.faewulf.diversity.event.playerChatEmote::run));
    }
}
