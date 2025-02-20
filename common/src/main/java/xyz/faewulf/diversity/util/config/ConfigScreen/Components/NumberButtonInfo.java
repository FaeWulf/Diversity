package xyz.faewulf.diversity.util.config.ConfigScreen.Components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation;

import static xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen.CONFIG_VALUES;

public class NumberButtonInfo extends StringWidget {

    private final ConfigLoaderFromAnnotation.EntryInfo entryInfo;
    private final Component initMessage;

    public NumberButtonInfo(int width, int height, Component message, Font font, ConfigLoaderFromAnnotation.EntryInfo info) {
        super(width, height, message, font);
        this.entryInfo = info;
        this.initMessage = message;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Change info text if value is changing
        Component valueStatusIndicator = initMessage;

        if (isChanging()) {
            valueStatusIndicator = Component.literal(initMessage.getString()).withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW);
        }

        setMessage(valueStatusIndicator);

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    private boolean isChanging() {
        Object value;
        try {
            value = this.entryInfo.targetField.get(null);
        } catch (IllegalAccessException e) {
            Constants.LOG.error("[Diversity] Something went wrong with the Option button...");
            e.printStackTrace();
            return false;
        }

        Object lastValue = CONFIG_VALUES.get(this.entryInfo.name);

        return !value.equals(lastValue);
    }
}
