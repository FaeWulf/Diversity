package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.AABB;
import xyz.faewulf.diversity.inter.ICustomDisplayEntity;
import xyz.faewulf.diversity.mixin.shulkerBoxLabel.DisplayEntityMixin;
import xyz.faewulf.diversity.mixin.shulkerBoxLabel.TextDisplayEntityMixin;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.List;

public class placeShulkerBlock {
    public static void run(Level world, Player player, ItemStack itemStack, BlockPos blockPos) {

        if (!ModConfigs.shulker_label)
            return;

        if (player == null || itemStack.isEmpty())
            return;

        if (world.isClientSide)
            return;

        //ignore if sneaking
        if (player.isShiftKeyDown())
            return;

        BlockEntity blockAt = world.getBlockEntity(blockPos);

        if (blockAt instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {

            if (!itemStack.hasCustomHoverName())
                return;

            Component text = itemStack.getHoverName();

            //if don't have custom name
            if (text == null)
                return;

            try {

                AABB box = new AABB(
                        blockPos.getX() + 0.3f, blockPos.getY() + 0.75f, blockPos.getZ() + 0.3f,
                        blockPos.getX() + 0.7f, blockPos.getY() + 1.05f, blockPos.getZ() + 0.7f
                );

                List<Entity> entitiesWithinRadius = world.getEntitiesOfClass(Entity.class, box, entity -> entity.getType() == EntityType.TEXT_DISPLAY);

                if (!entitiesWithinRadius.isEmpty())
                    return;

                Display.TextDisplay w = new Display.TextDisplay(EntityType.TEXT_DISPLAY, player.level());

                w.setCustomName(text);
                w.setCustomNameVisible(true);

                ((TextDisplayEntityMixin) w).invokeSetBackground(1174405120);
                ((DisplayEntityMixin) w).invokeSetBillboardMode(Display.BillboardConstraints.CENTER);

                ((ICustomDisplayEntity) w).multiLoader_1_20_1$setType(1);

                w.setPos(blockPos.getX() + 0.5f, blockPos.getY() + 0.95f, blockPos.getZ() + 0.5f);

                world.addFreshEntity(w);
            } catch (ClassCastException classCastException) {
                return;
            }

        }
    }
}
