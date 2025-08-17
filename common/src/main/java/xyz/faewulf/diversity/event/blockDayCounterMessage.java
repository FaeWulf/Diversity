package xyz.faewulf.diversity.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class blockDayCounterMessage {
    public static boolean run(Component message) {
        if (!ModConfigs.blacklist_day_counter)
            return true;

        System.out.println("1");
        System.out.println(message.getStyle());

        if (message.getStyle().getHoverEvent() != null) {
            HoverEvent hover = message.getStyle().getHoverEvent();
            System.out.println("2");
            System.out.println(hover);

            if (hover.action() == HoverEvent.Action.SHOW_TEXT || hover instanceof HoverEvent.ShowText) {
                System.out.println("3");
                System.out.println(((HoverEvent.ShowText) hover).value());

                // Block day counter message
                Component component = ((HoverEvent.ShowText) hover).value();
                if (component != null && component.getString().equals(Constants.MOD_ID + "_day-counter")) {
                    return false;
                }
            }
        }

        return true;
    }
}
