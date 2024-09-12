package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.faewulf.diversity.inter.ICustomBundleItem;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class changeBundleMode {
    static public InteractionResult run(Level world, Player player, InteractionHand hand, BlockPos pos, Direction direction) {

        if (!ModConfigs.bundle_place_mode)
            return InteractionResult.PASS;

        if (world.isClientSide)
            return InteractionResult.PASS;

        if (player instanceof ServerPlayer serverPlayer) {
            if (hand == InteractionHand.MAIN_HAND) {
                ItemStack stack = serverPlayer.getMainHandItem();
                if (!stack.isEmpty()) {
                    if (stack.getItem() instanceof BundleItem bundleItem && serverPlayer.isShiftKeyDown()) {

                        if (serverPlayer.getCooldowns().isOnCooldown(stack.getItem())) {
                            return InteractionResult.PASS;
                        }

                        int mode = ((ICustomBundleItem) bundleItem).getMode(stack);

                        mode++;
                        if (mode > 2)
                            mode = 0;

                        ((ICustomBundleItem) bundleItem).setMode(stack, mode);

                        String modeText = switch (mode) {
                            case 1 -> "Place first slot";
                            case 2 -> "Place random slot";
                            default -> "Normal";
                        };

                        serverPlayer.displayClientMessage(Component.literal("Changed mode to: " + modeText), true);
                        world.playSound(null, serverPlayer.blockPosition(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 0.5f, 1.5f);
                        serverPlayer.getCooldowns().addCooldown(stack.getItem(), 20);

                        return InteractionResult.CONSUME;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }
}
