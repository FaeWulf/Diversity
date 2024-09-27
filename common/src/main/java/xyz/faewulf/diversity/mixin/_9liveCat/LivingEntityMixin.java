package xyz.faewulf.diversity.mixin._9liveCat;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.inter.entity.ICustomCatEntity;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public abstract ItemStack getItemInHand(InteractionHand hand);

    @Shadow
    public abstract void setItemInHand(InteractionHand hand, ItemStack stack);

    @Inject(method = "checkTotemDeathProtection", at = @At(value = "HEAD"))
    private void checkTotemDeathProtectionInject(DamageSource source, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs._9_lives_cat)
            return;

        if (this instanceof ICustomCatEntity iCustomCatEntity) {
            if (iCustomCatEntity.diversity_Multiloader$getLives() > 0) {
                iCustomCatEntity.diversity_Multiloader$setLives(iCustomCatEntity.diversity_Multiloader$getLives() - 1);
                ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
                totem.setCount(1);
                this.setItemInHand(InteractionHand.MAIN_HAND, totem);
            }
        }
    }
}
