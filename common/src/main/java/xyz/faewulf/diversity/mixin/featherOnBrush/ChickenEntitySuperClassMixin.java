package xyz.faewulf.diversity.mixin.featherOnBrush;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.inter.entity.ICustomChickenEntity;
import xyz.faewulf.diversity.util.ModConfigs;

import static net.minecraft.world.entity.LivingEntity.getSlotForHand;

@Mixin(Animal.class)
public class ChickenEntitySuperClassMixin {


    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void InjectInteractMod(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        if (!ModConfigs.brushable_parrot_chicken)
            return;

        if ((Object) this instanceof Chicken chickenEntity) {
            if (player.level().isClientSide || ((ICustomChickenEntity) chickenEntity).getFeatherCoolDown() > 0)
                return;

            if (chickenEntity.isBaby() || !player.isShiftKeyDown() || player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != Items.BRUSH)
                return;

            player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, player, getSlotForHand(InteractionHand.MAIN_HAND));
            chickenEntity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 0.8f, 1.0f);

            ItemStack drops = new ItemStack(Items.FEATHER);
            drops.setCount(chickenEntity.getRandom().nextIntBetweenInclusive(1, 2));
            chickenEntity.spawnAtLocation(drops);

            ((ICustomChickenEntity) chickenEntity).setFeatherCoolDown(12000);
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }


    }

}
