package xyz.faewulf.diversity.util.config.infoScreen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModInfoScreen extends Screen {

    private static final ResourceLocation MAIN_IMAGE = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/d.png");
    private static final ResourceLocation LIGHT_RAYS = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/light_rays.png");
    private final Screen parent;
    private final Minecraft client;

    private final List<rainITem> fallingEntities = new ArrayList<>();

    //background
    public static final ResourceLocation ATLAS_TEXTURE = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/atlas_background.png");

    //background
    private final int ATLAS_SIZE = 32 * 3; // number of atlas tile
    private final int TILE_SIZE = 32;
    private int[][] tileMap;  // Store the (x, y) positions of the random tiles in the atlas
    private int tilesX;
    private int tilesY;

    //logo
    private final float logo_offset_Y = 0.6f;

    //comps
    private GridLayout buttonLayout;
    private GridLayout infoLayout;
    private Button settingButton;

    private float time = 0.0f;  // Time variable to track animation

    protected ModInfoScreen(Screen parent) {
        super(Component.literal("Diversity info"));
        this.parent = parent;
        client = Minecraft.getInstance();

        this.tilesX = (int) Math.ceil(this.width * 1.0f / TILE_SIZE);
        this.tilesY = (int) Math.ceil(this.height * 1.0f / TILE_SIZE);
        this.tileMap = new int[tilesX][tilesY];
    }

    public static Screen getScreen(Screen parent) {
        return new ModInfoScreen(parent);
    }

    @Override
    protected void init() {
        //background
        generateRandomTileMap();

        //rain
        for (int i = 0; i < 25; i++) {  // Example for 100 falling entities
            int texture = i % rainITem.itemSize;
            float x = (float) (Math.random() * this.width);
            float y = (float) (Math.random() * this.height);
            float velocityX = (float) (Math.random() * 1f) - 0.5f;  // Random blow speed
            float velocityY = 1f + (float) (Math.random() * 1f);  // Random falling speed
            float rotationSpeed = (float) ((Math.random() * 14) - 7);  // Random rotation speed
            fallingEntities.add(new rainITem(texture, x, y, velocityX, velocityY, rotationSpeed, this.width, this.height));
        }

        //buttons
        this.buttonLayout = new GridLayout();
        this.infoLayout = new GridLayout();

        GridLayout.RowHelper rowHelper = buttonLayout.createRowHelper(1);
        rowHelper.defaultCellSetting().padding(4);

        settingButton = rowHelper.addChild(
                Button.builder(
                        Component.literal("Configuration..."),
                        button -> this.client.setScreen(ConfigScreen.getScreen(this))).build()
        );

        rowHelper.addChild(
                Button.builder(
                        Component.literal("Close"),
                        button -> this.onClose()).build()
        );

        GridLayout.RowHelper rowHelperInfoLayout = infoLayout.createRowHelper(2);
        rowHelperInfoLayout.defaultCellSetting().padding(2);

        rowHelperInfoLayout.addChild(
                Button.builder(
                                Component.literal("Website"),
                                button -> this.openWebLink("https://faewulf.xyz/"))
                        .width(50).build()
        );

        rowHelperInfoLayout.addChild(
                Button.builder(
                                Component.literal("Discord").withStyle(ChatFormatting.BLUE),
                                button -> this.openWebLink("https://discord.com/invite/xZneCTcEvb"))
                        .width(50).build()
        );

        //add comp to screen renderer
        buttonLayout.visitWidgets(this::addRenderableWidget);
        infoLayout.visitWidgets(this::addRenderableWidget);

        //init reposition
        repositionElements();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        //background
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        settingButton.setFocused(false);
        // Render other screen elements (if any)
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        drawRandomTiledBackground(guiGraphics);

        randomShtShowering(guiGraphics, pPartialTick);

        guiGraphics.fillGradient(
                0,
                0,
                this.width,
                this.height,
                0xaa000000, 0x80000000
        );

        // Update time for animation (delta ensures smooth animation)
        time += pPartialTick * 0.5f * 0.05f;

        // Draw the light rays behind the image
        drawLightRays(guiGraphics);

        // Draw the wobbling main image in the center
        drawWobblingImage(guiGraphics);
    }

    private void drawLightRays(GuiGraphics guiGraphics) {
        int centerX = this.width / 2;
        int centerY = (int) (this.height * this.logo_offset_Y / 2);
        int size = 128;  // Size of the light ray texture

        float rotationAngle = time * 20.0f;

        // Apply rotation and scale transformations
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();  // Save the current transformation state

        float wobbleOffsetY = (float) Math.sin(time) * 5.0f;  // 5-pixel wobble effect

        // Translate to the center of the screen
        // Apply rotation (in degrees)

        matrixStack.translate(centerX, centerY + wobbleOffsetY, 0);
        matrixStack.mulPose(Axis.ZP.rotationDegrees(rotationAngle));
        guiGraphics.pose().translate(-size / 2f, -size / 2f, 0);  // Move to center + wobble

        // Apply breathing scale

        // Draw light rays, slightly scaled up and centered
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(
                LIGHT_RAYS,
                0, 0,
                0, 0,
                size, size,
                size, size
        );
        RenderSystem.disableBlend();

        matrixStack.popPose();
    }

    private void drawWobblingImage(GuiGraphics guiGraphics) {
        int imageSize = 64;  // Size of the image
        int centerX = this.width / 2;
        int centerY = (int) ((this.height * logo_offset_Y) / 2);

        // Wobble effect: Use sine wave to make the image wobble up and down
        float wobbleOffsetY = (float) Math.sin(time) * 5.0f;  // 10-pixel wobble effect

        // Apply wobble effect using MatrixStack transformations
        guiGraphics.pose().pushPose();  // Save the current transformation state
        guiGraphics.pose().translate(centerX, centerY + wobbleOffsetY, 0);  // Move to center + wobble
        //guiGraphics.pose().scale(wobbleScale, wobbleScale, 1.0f);  // Apply wobble scaling

        // Render the image at the center, adjusted for the wobble
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(
                MAIN_IMAGE,
                -imageSize / 2,  // Center the image on the X axis
                -imageSize / 2,  // Center the image on the Y axis
                0, 0,
                imageSize, imageSize,
                imageSize, imageSize
        );
        RenderSystem.disableBlend();

        guiGraphics.pose().popPose();  // Restore the original transformation state
    }


    @Override
    protected void repositionElements() {
        this.tilesX = (int) Math.ceil(this.width * 1.0f / TILE_SIZE);
        this.tilesY = (int) Math.ceil(this.height * 1.0f / TILE_SIZE);
        this.tileMap = new int[tilesX][tilesY];

        generateRandomTileMap();

        if (this.buttonLayout != null && this.infoLayout != null) {
            this.buttonLayout.arrangeElements();
            this.infoLayout.arrangeElements();
            FrameLayout.alignInRectangle(this.buttonLayout, 0, 0, this.width, this.height, 0.5f, 0.8f);
            FrameLayout.alignInRectangle(this.infoLayout, 0, 0, this.width, this.height, 1f, 1f);
        }

        fallingEntities.forEach(rainITem -> rainITem.updateScreenSize(this.width, this.height));
    }


    @Override
    public void onClose() {
        if (this.client != null)
            this.client.setScreen(this.parent);
        else
            super.onClose();
    }

    private void randomShtShowering(GuiGraphics guiGraphics, float delta) {
        //guiGraphics.renderTooltip(this.font, Component.literal("Tsting the new system"), 50, 50);
        // Update and render each falling entity
        for (rainITem entity : fallingEntities) {
            entity.update(delta);
            entity.render(guiGraphics, delta);
        }
    }

    private void drawRandomTiledBackground(GuiGraphics guiGraphics) {
        int tilesPerRow = ATLAS_SIZE / TILE_SIZE;  // Number of tiles per row in the atlas

        for (int y = 0; y < tilesY; y++) {
            for (int x = 0; x < tilesX; x++) {
                // Calculate the tile's atlas coordinates based on the stored index
                int tileIndex = tileMap[x][y];
                int tileX = (tileIndex % tilesPerRow) * TILE_SIZE;  // X offset in the atlas
                int tileY = (tileIndex / tilesPerRow) * TILE_SIZE;  // Y offset in the atlas

                // Draw the tile from the atlas
                guiGraphics.blit(
                        ATLAS_TEXTURE,
                        x * TILE_SIZE,  // X position on the screen
                        y * TILE_SIZE,  // Y position on the screen
                        tileX, tileY,   // X, Y offset in the atlas
                        TILE_SIZE, TILE_SIZE,  // Tile size (width, height)
                        ATLAS_SIZE, ATLAS_SIZE  // Atlas size (width, height)
                );
            }
        }
    }

    private void generateRandomTileMap() {
        Random random = new Random();
        int tilesPerRow = ATLAS_SIZE / TILE_SIZE;  // Number of tiles per row in the atlas

        for (int y = 0; y < tilesY; y++) {
            for (int x = 0; x < tilesX; x++) {
                // Randomly select a tile from the atlas by generating random coordinates (tileX, tileY)
                int tileX = random.nextInt(tilesPerRow);  // Random column
                int tileY = random.nextInt(tilesPerRow);  // Random row

                // Store the (tileX, tileY) for later use when rendering
                tileMap[x][y] = tileY * tilesPerRow + tileX;  // Linear index for simplicity
            }
        }
    }

    private void openWebLink(String url) {
        this.client.setScreen(new ConfirmLinkScreen(
                confirmed -> {
                    if (confirmed) {
                        // Open the URL if the player confirms
                        Util.getPlatform().openUri(url);
                    }
                    // Return to the previous screen if canceled
                    this.client.setScreen(this);
                },
                url,  // URL to open
                true  // Show the "Copy to Clipboard" button
        ));
    }
}
