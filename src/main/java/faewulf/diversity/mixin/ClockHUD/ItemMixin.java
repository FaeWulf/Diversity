package faewulf.diversity.mixin.ClockHUD;

import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.converter;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin implements ToggleableFeature, ItemConvertible, FabricItem {


    @Shadow
    public abstract Item asItem();

    @Inject(method = "use", at = @At("TAIL"))
    private void useInject(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

        if (!ModConfigs.check_villager_schedule)
            return;

        if (world.isClient)
            return;

        if (this.asItem() == Items.CLOCK) {
            user.sendMessage(Text.literal("\uD83D\uDD59 " + converter.tick2MinecraftTime(world.getTimeOfDay())).formatted(Formatting.GOLD), true);
        }
    }
}
