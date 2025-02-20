package xyz.faewulf.diversity.util.config.ConfigScreen.Components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation;

import java.lang.reflect.Field;

public class NumberButton extends EditBox {

    private final ConfigLoaderFromAnnotation.EntryInfo entryInfo;

    private boolean isFirstTime = true;

    public NumberButton(Font font, int width, int height, Component message, ConfigLoaderFromAnnotation.EntryInfo entryInfo) {
        super(font, width, height, message);

        this.entryInfo = entryInfo;

        this.setResponder(this::onTextChange);
        this.setHint(Component.literal("Number only"));
        // Set value to edit box
        try {
            Object object = entryInfo.targetField.get(null);
            this.setValue(object.toString());
        } catch (IllegalAccessException e) {
            Constants.LOG.error("[Diversity] Something went wrong with the Number button...");
            e.printStackTrace();
        }

    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!Character.isDigit(codePoint)) {
            return false; // Ignore non-numeric input
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        // Set init value to edit box
        // Some how do this rather than in init<> will shows the text, in init<> it won't show value text until you click on it
        try {
            Object object = entryInfo.targetField.get(null);

            if (!this.getValue().equals(object.toString()) || isFirstTime) {
                isFirstTime = false;
                this.setValue(object.toString());
            }

        } catch (IllegalAccessException e) {
            Constants.LOG.error("[Diversity] Something went wrong with the Number button...");
            e.printStackTrace();
        }

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void onTextChange(String filter) {
        Field field = entryInfo.targetField;
        try {
            field.set(null, Integer.parseInt(filter));
        } catch (IllegalAccessException | NumberFormatException e) {
            Constants.LOG.error("[Diversity] Something went wrong with the config system...");
            e.printStackTrace();
        }
    }
}
