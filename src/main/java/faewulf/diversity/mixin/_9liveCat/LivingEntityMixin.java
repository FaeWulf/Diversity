package faewulf.diversity.mixin._9liveCat;

import faewulf.diversity.inter.entity.ICustomCatEntity;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    public abstract void setStackInHand(Hand hand, ItemStack stack);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = "tryUseTotem", at = @At(value = "HEAD"))
    private void tryUseTotemInject(DamageSource source, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs._9_lives_cat)
            return;

        if (this instanceof ICustomCatEntity iCustomCatEntity) {
            if (iCustomCatEntity.getLives() > 0) {
                iCustomCatEntity.setLives(iCustomCatEntity.getLives() - 1);
                ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
                totem.setCount(1);
                this.setStackInHand(Hand.MAIN_HAND, totem);
            }
        }
    }
}
