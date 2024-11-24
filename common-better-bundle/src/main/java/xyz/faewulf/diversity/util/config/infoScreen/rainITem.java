package xyz.faewulf.diversity.util.config.infoScreen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import xyz.faewulf.diversity.Constants;

public class rainITem {
    public static final int itemSize = 9;
    private static final ResourceLocation RAIN_ITEM = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/rain_item.png");
    private final int texture;  // Texture for the falling entity
    private final float velocityX;  // Falling speed
    private final float velocityY;  // Falling speed
    private final float rotationSpeed;  // Speed of rotation
    private final int ITEM_SIZE = 16;
    private float x, y;  // Position
    private float rotationAngle;  // Rotation angle
    private int screenWidth, screenHeight;

    public rainITem(int item, float x, float y, float velocityX, float velocityY, float rotationSpeed, int screenWidth, int screenHeight) {
        this.texture = item;
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.rotationSpeed = rotationSpeed;
        this.rotationAngle = 0;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void updateScreenSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update(float delta) {
        // Update position
        x += velocityX * delta;
        y += velocityY * delta;

        // Update rotation
        rotationAngle += rotationSpeed * delta;
        if (rotationAngle >= 360) {
            rotationAngle -= 360;
        }

        if (rotationAngle <= 0) {
            rotationAngle += 360;
        }

        // If the item falls off the screen, reset its position to the top
        if (y > screenHeight + 32) {
            y = -16;  // Start above the screen
            x = (float) (Math.random() * screenWidth);  // Randomize the x position
        }

        if (x > screenWidth + 48)
            x = -32;

        if (x < -32)
            x = screenWidth + 48;

    }

    public void render(GuiGraphics guiGraphics, float delta) {

        // Apply transformations
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x + ITEM_SIZE / 2f, y + ITEM_SIZE / 2f, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationAngle));
        poseStack.translate(-ITEM_SIZE / 2f, -ITEM_SIZE / 2f, 0);

        // Draw the texture

        guiGraphics.blit(
                RAIN_ITEM,
                0,  // X position on the screen
                0,  // Y position on the screen
                (float) (texture % 3) * ITEM_SIZE, (float) Math.floor(texture / 3f) * ITEM_SIZE,   // X, Y offset in the atlas
                ITEM_SIZE, ITEM_SIZE,  // Tile size (width, height)
                48, 48  // Atlas size (width, height)
        );

        poseStack.popPose();
    }
}
