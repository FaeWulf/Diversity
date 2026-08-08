package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;

public class hydrophobicElytra {
    public static void register() {
        EntityElytraEvents.CUSTOM.register(((entity, tickElytra) -> xyz.faewulf.diversity.event.hydrophobicElytra.run(entity)));
    }
}
