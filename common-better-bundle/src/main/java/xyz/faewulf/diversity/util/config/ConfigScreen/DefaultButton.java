package xyz.faewulf.diversity.util.config.ConfigScreen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class DefaultButton extends Button {
    public DefaultButton(int x, int y, int width, int height, Component Text, OnPress onPress) {
        super(x, y, width, height, Text, onPress, DEFAULT_NARRATION);
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void setFocused(boolean $$0) {
        super.setFocused(false);
    }

    @Override
    public void renderString(GuiGraphics graphics, Font textRenderer, int color) {
        Font font = Minecraft.getInstance().font;
        PoseStack pose = graphics.pose();

        float scale = 2f;  // Set the scale (2.0f means 2x size)
        int textWidth = textRenderer.width(this.getMessage());
        int textHeight = textRenderer.lineHeight;

        // Center the text on the button
        int textX = this.getX() + 1 + (this.width - (int) (textWidth * scale)) / 2;
        int textY = this.getY() - 1 + (this.height - (int) (textHeight * scale)) / 2;

        pose.pushPose();
        pose.scale(scale, scale, 1);
        pose.translate((textX / scale), (textY / scale), 0);
        graphics.drawString(font, getMessage(), 0, 0, color | 0xFFFFFF, true);
        pose.popPose();
    }
}
