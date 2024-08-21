package faewulf.diversity.mixin.TorchFireAttack;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void damageInject(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.torch_burn_target)
            return;

        Entity entityAttacker = source.getAttacker();

        if (entityAttacker != null) {
            if (entityAttacker instanceof LivingEntity livingEntity) {
                livingEntity.getHandItems().forEach(itemStack -> {
                    if (itemStack.getItem() == Items.TORCH || itemStack.getItem() == Items.SOUL_TORCH) {
                        this.setOnFireFor(5);
                        return;
                    }
                });
            }
        }
    }
}
