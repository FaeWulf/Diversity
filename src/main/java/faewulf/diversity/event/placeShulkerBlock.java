package faewulf.diversity.event;

import faewulf.diversity.callback.BlockPlacedCallback;
import faewulf.diversity.inter.ICustomDisplayEntity;
import faewulf.diversity.mixin.shulkerBoxLabel.DisplayEntityMixin;
import faewulf.diversity.mixin.shulkerBoxLabel.TextDisplayEntityMixin;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class placeShulkerBlock {

    public static void run() {

        if (!ModConfigs.shulker_label)
            return;

        BlockPlacedCallback.EVENT.register(placeShulkerBlock::check);
    }

    private static void check(ItemPlacementContext context) {

        PlayerEntity playerEntity = context.getPlayer();

        if (playerEntity == null || context.getStack().isEmpty())
            return;

        if (playerEntity.getWorld().isClient)
            return;

        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        ServerWorld serverWorld = (ServerWorld) playerEntity.getWorld();
        BlockEntity blockAt = serverWorld.getBlockEntity(blockPos);

        if (blockAt instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {

            Text text = itemStack.get(DataComponentTypes.CUSTOM_NAME);
            //if don't have custom name
            if (text == null)
                return;

            DisplayEntity.TextDisplayEntity w = new DisplayEntity.TextDisplayEntity(EntityType.TEXT_DISPLAY, playerEntity.getWorld());

            w.setCustomName(text);
            w.setCustomNameVisible(true);

            ((TextDisplayEntityMixin) w).invokeSetBackground(1174405120);
            ((DisplayEntityMixin) w).invokeSetBillboardMode(DisplayEntity.BillboardMode.CENTER);

            ((ICustomDisplayEntity) w).setType(1);

            w.setPosition(blockPos.getX() + 0.5f, blockPos.getY() + 0.95f, blockPos.getZ() + 0.5f);

            serverWorld.spawnEntity(w);
        }
    }
}
