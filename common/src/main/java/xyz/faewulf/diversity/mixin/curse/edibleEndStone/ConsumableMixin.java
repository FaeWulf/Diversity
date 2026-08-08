package xyz.faewulf.diversity.mixin.curse.edibleEndStone;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Consumable.class)
public abstract class ConsumableMixin {

    @Inject(method = "onConsume",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
    private void eatFoodInject(Level level, LivingEntity livingEntity, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {

        if (level.isClientSide())
            return;

        if (!ModConfigs.endstone_is_cheese)
            return;

        //chance to clea effect
        if (itemStack.getItem() == Items.END_STONE && livingEntity.getRandom().nextFloat() < 0.2) {
            livingEntity.removeAllEffects();
        }
    }
}
