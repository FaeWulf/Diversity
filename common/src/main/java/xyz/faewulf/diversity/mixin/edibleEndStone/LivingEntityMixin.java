package xyz.faewulf.diversity.mixin.edibleEndStone;

import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow
    public abstract boolean removeAllEffects();

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEatEffect(Lnet/minecraft/world/food/FoodProperties;)V"))
    private void eatFoodInject(Level world, ItemStack stack, FoodProperties foodComponent, CallbackInfoReturnable<ItemStack> cir) {

        if (world.isClientSide)
            return;

        if (!ModConfigs.endstone_is_cheese)
            return;

        //chance to clea effect
        if (stack.getItem() == Items.END_STONE && this.random.nextFloat() < 0.2) {
            this.removeAllEffects();
        }
    }
}
