package xyz.faewulf.diversity.util.config.ConfigScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation;

import java.util.Objects;

import static xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen.CONFIG_VALUES;

public class OptionButton extends Button {

    private final ConfigLoaderFromAnnotation.EntryInfo entryInfo;

    //text scroll effect
    private long lastTime;
    private int scrollOffset;
    private boolean reverse = false;
    private int effectCooldown = 0;

    public OptionButton(int x, int y, int width, int height, Component message, Component tooltip, OnPress onPress, ConfigLoaderFromAnnotation.EntryInfo entryInfo) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.entryInfo = entryInfo;

        if (tooltip != null)
            setTooltip(Tooltip.create(tooltip));

        this.lastTime = System.currentTimeMillis();
        this.scrollOffset = 0;
    }

    public OptionButton(int x, int y, int width, int height, Component message, OnPress onPress, ConfigLoaderFromAnnotation.EntryInfo entryInfo) {
        this(x, y, width, height, message, null, onPress, entryInfo);
    }

    @Override
    public void renderString(GuiGraphics graphics, @NotNull Font textRenderer, int color) {
        Font font = Minecraft.getInstance().font;

        String leftValue = getMessage().getString();
        String rightValue;
        try {
            Object object = entryInfo.targetField.get(null);
            rightValue = object.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Component component = Component.literal(rightValue);

        if (rightValue.equalsIgnoreCase("true")) {
            component = Component.literal("✔").withStyle(ChatFormatting.GREEN);
            rightValue = "✔";
        }

        if (rightValue.equalsIgnoreCase("false")) {
            component = Component.literal("\u274C").withStyle(ChatFormatting.RED);
            rightValue = "\u274C";
        }

        // Calculate text positions for left and right values
        int leftTextX = this.getX() + 4;  // Left-aligned, 4 pixels from the left edge
        int rightTextX = this.getX() + this.width - textRenderer.width(rightValue) - 4;  // Right-aligned, 4 pixels from the right edge
        int textY = (int) (this.getY() + (this.height - textRenderer.lineHeight) * 1.1f / 2);  // Vertically centered for both

        // Draw the left and right values

        graphics.drawString(font, component, rightTextX, textY, 0xFFFFFF);  // Right value

        String displayedLeftValue = trimTextWithEllipsis(leftValue, (int) ((this.width - 4) * 0.7), font);

        Component leftTextComp = Component.literal(displayedLeftValue);
        if (isChanging())
            leftTextComp = Component.literal(displayedLeftValue).withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW);

        graphics.drawString(font, leftTextComp, leftTextX, textY, 0xFFFFFF);  // Left value
    }

    @Override
    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        super.render($$0, $$1, $$2, $$3);

        //hover effect
        if (isMouseOver($$1, $$2) && !Objects.equals(this.entryInfo.name, ConfigScreen.currentInfo)) {

            ConfigScreen.infoTab_Title.setMessage(Component.literal(this.entryInfo.humanizeName).withStyle(ChatFormatting.BOLD));

            MutableComponent info = Component.translatable("diversity.config." + this.entryInfo.name + ".tooltip");

            if (this.entryInfo.require_restart)
                info.append(Component.literal("\n\n").append(Component.translatable("diversity.config.require_restart").withStyle(ChatFormatting.GOLD)));

            ConfigScreen.infoTab_Info.setMessage(info);
            ConfigScreen.infoTab.arrangeElements();
            ConfigScreen.currentInfo = this.entryInfo.name;
        }
    }

    private boolean isChanging() {
        Object value;
        try {
            value = this.entryInfo.targetField.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Object lastValue = CONFIG_VALUES.get(this.entryInfo.name);

        return !value.equals(lastValue);
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void setFocused(boolean $$0) {
        super.setFocused(false);
    }

    // Trims text to fit within the specified width and adds ellipsis if necessary
    private String trimTextWithEllipsis(String text, int maxWidth, Font textRenderer) {
        if (textRenderer.width(text) <= maxWidth) {
            return text;  // Text fits, no need to trim
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > 150) {  // Scroll every 150ms (slow scroll effect)

            if (effectCooldown > 0) {
                effectCooldown--;
                String scrollingText = getScrollingText(text, this.scrollOffset, maxWidth, textRenderer);
                return textRenderer.plainSubstrByWidth(scrollingText, maxWidth - textRenderer.width("...")) + "...";
            }

            //reverse offset value
            if (!reverse)
                this.scrollOffset = (this.scrollOffset + 1) % text.length();
            else
                this.scrollOffset = (this.scrollOffset - 1) % text.length();

            //this one will check if trimmed text's width < max width, then stop the trim and reverse the effect
            //to prevent the text fly out (disappear) of the button, leaves empty button in the process, imo that is ugly

            //the 2nd criteria is reset animation if scrolloffset back to 0
            String checkText = getScrollingText(text, this.scrollOffset, maxWidth, textRenderer);
            if (textRenderer.width(checkText) + 6 < maxWidth || this.scrollOffset < 1) {
                reverse = !reverse;
                //set cooldown to make the text stay for a while
                this.effectCooldown = 90;
            }
            lastTime = currentTime;
        }


        String scrollingText = getScrollingText(text, this.scrollOffset, maxWidth, textRenderer);
        return textRenderer.plainSubstrByWidth(scrollingText, maxWidth - textRenderer.width("...")) + "...";
    }

    // Generates a substring of the text for scrolling
    private String getScrollingText(String text, int scrollOffset, int maxWidth, Font textRenderer) {
        // Get a substring of the text starting from the current scroll offset

        if (scrollOffset < 0)
            scrollOffset = 0;

        if (scrollOffset > text.length())
            scrollOffset = text.length();

        String visiblePart = text.substring(scrollOffset);

        // Trim the visible part to fit in the available width
        if (textRenderer.width(visiblePart) > maxWidth) {
            visiblePart = textRenderer.plainSubstrByWidth(visiblePart, maxWidth);
        }

        return visiblePart;
    }
}
