package faewulf.diversity.mixin.GlowBerries;

import faewulf.diversity.util.ModConfigs;

//? if >=1.21 {
/*import net.minecraft.component.type.FoodComponent;
*///?}

import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //? if =1.20.1 {
    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyFoodEffects(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V"))
    private void eatFoodInject(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {

        if (world.isClient)
            return;

        if (!ModConfigs.glow_berry_glowing)
            return;

        if (stack.getItem() == Items.GLOW_BERRIES) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100));
        }
    }
    //?}

    //? if >=1.21 {
    
    /*@Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyFoodEffects(Lnet/minecraft/component/type/FoodComponent;)V"))
    private void eatFoodInject(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {

        if (world.isClient)
            return;

        if (!ModConfigs.glow_berry_glowing)
            return;

        if (stack.getItem() == Items.GLOW_BERRIES) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100));
        }
    }
     
    *///?}
}
