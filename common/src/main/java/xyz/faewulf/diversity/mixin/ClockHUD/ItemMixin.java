package xyz.faewulf.diversity.mixin.ClockHUD;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.converter;

@Mixin(Item.class)
public abstract class ItemMixin implements FeatureElement, ItemLike {


    @Shadow
    public abstract Item asItem();

    @Inject(method = "use", at = @At("TAIL"))
    private void useInject(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {

        if (!ModConfigs.clock_shows_time)
            return;

        if (world.isClientSide)
            return;

        if (this.asItem() == Items.CLOCK) {
            user.displayClientMessage(Component.literal("\uD83D\uDD59 " + converter.tick2MinecraftTime(world.getDayTime())).withStyle(ChatFormatting.GOLD), true);
        }
    }
}
