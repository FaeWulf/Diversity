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
import xyz.faewulf.diversity.util.config.ModConfigs;

import static xyz.faewulf.diversity.util.MissingMethod.LivingEntityMethod.getSlotForHand;

@Mixin(Animal.class)
public class ChickenEntitySuperClassMixin {


    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void InjectInteractMod(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        if (!ModConfigs.brushable_parrot_chicken)
            return;

        if ((Object) this instanceof Chicken chickenEntity) {
            if (player.level().isClientSide || ((ICustomChickenEntity) chickenEntity).multiLoader_1_20_1$getFeatherCoolDown() > 0)
                return;

            if (chickenEntity.isBaby() || !player.isShiftKeyDown() || player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != Items.BRUSH)
                return;

            //Equivalent for
            //player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, player, getSlotForHand(InteractionHand.MAIN_HAND));
            //:

            player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, player, $$1x -> $$1x.broadcastBreakEvent(getSlotForHand(InteractionHand.MAIN_HAND)));
            chickenEntity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 0.8f, 1.0f);

            ItemStack drops = new ItemStack(Items.FEATHER);
            drops.setCount(chickenEntity.getRandom().nextIntBetweenInclusive(1, 2));
            chickenEntity.spawnAtLocation(drops);

            ((ICustomChickenEntity) chickenEntity).multiLoader_1_20_1$setFeatherCoolDown(12000);
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }


    }

}
