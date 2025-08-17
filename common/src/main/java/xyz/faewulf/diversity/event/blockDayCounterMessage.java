package xyz.faewulf.diversity.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class blockDayCounterMessage {
    public static boolean run(Component message) {
        if (!ModConfigs.blacklist_day_counter)
            return true;

        if (message.getStyle().getHoverEvent() != null) {
            HoverEvent hover = message.getStyle().getHoverEvent();

            if (hover.getAction() == HoverEvent.Action.SHOW_TEXT) {
                // Block day counter message
                Component component = hover.getValue(HoverEvent.Action.SHOW_TEXT);
                if (component != null && component.getString().equals(Constants.MOD_ID + "_day-counter")) {
                    return false;
                }
            }
        }

        return true;
    }
}
