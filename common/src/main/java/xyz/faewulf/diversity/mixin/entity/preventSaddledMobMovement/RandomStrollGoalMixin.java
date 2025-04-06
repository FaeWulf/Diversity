package xyz.faewulf.diversity.mixin.entity.preventSaddledMobMovement;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(RandomStrollGoal.class)
public abstract class RandomStrollGoalMixin extends Goal {

    @Shadow
    @Final
    protected PathfinderMob mob;

    @Inject(method = "getPosition", at = @At("HEAD"), cancellable = true)
    private void getPositionInject(CallbackInfoReturnable<Vec3> cir) {
        if (ModConfigs.prevent_tamed_horse_wandering) {
            ItemStack itemStack = this.mob.getItemBySlot(EquipmentSlot.SADDLE);
            if (!itemStack.isEmpty()) {
                cir.setReturnValue(null);
                cir.cancel();
            }
        }
    }
}
