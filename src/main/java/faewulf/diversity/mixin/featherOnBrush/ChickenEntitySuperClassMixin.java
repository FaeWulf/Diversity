package faewulf.diversity.mixin.featherOnBrush;

import faewulf.diversity.inter.entity.ICustomChickenEntity;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.entity.LivingEntity.getSlotForHand;

@Mixin(AnimalEntity.class)
public class ChickenEntitySuperClassMixin {


    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void InjectInteractMod(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

        if (!ModConfigs.brushable_parrot_chicken)
            return;

        if ((Object) this instanceof ChickenEntity chickenEntity) {
            if (player.getWorld().isClient || ((ICustomChickenEntity) chickenEntity).getFeatherCoolDown() > 0)
                return;

            if (chickenEntity.isBaby() || !player.isSneaking() || player.getStackInHand(Hand.MAIN_HAND).getItem() != Items.BRUSH)
                return;

            player.getStackInHand(Hand.MAIN_HAND).damage(1, player, getSlotForHand(Hand.MAIN_HAND));
            chickenEntity.playSound(SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, 0.8f, 1.0f);

            ItemStack drops = new ItemStack(Items.FEATHER);
            drops.setCount(chickenEntity.getRandom().nextBetween(1, 2));
            chickenEntity.dropStack(drops);

            ((ICustomChickenEntity) chickenEntity).setFeatherCoolDown(12000);
            cir.setReturnValue(ActionResult.SUCCESS);
            cir.cancel();
        }


    }

}
