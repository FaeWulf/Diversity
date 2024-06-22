package faewulf.diversity.event;

import faewulf.diversity.inter.ICustomBundleItem;
import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class changeBundleMode {
    static public void run() {

        AttackBlockCallback.EVENT.register(((player, world, hand, pos, direction) -> {

            if (!ModConfigs.bundle_place_mode)
                return ActionResult.PASS;

            if (world.isClient)
                return ActionResult.PASS;

            if (hand == Hand.MAIN_HAND) {
                ItemStack stack = player.getMainHandStack();
                if (stack != null && !stack.isEmpty()) {
                    if (stack.getItem() instanceof BundleItem bundleItem && player.isSneaking()) {

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

                        player.sendMessage(Text.literal("Change mode to: " + modeText), true);
                        world.playSound(null, pos, SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.PLAYERS, 0.5f, 1.5f);

                        return ActionResult.CONSUME;
                    }
                }
            }

            return ActionResult.PASS;
        }));
    }
}
