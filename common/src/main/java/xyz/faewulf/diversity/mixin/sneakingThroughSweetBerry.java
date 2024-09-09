package xyz.faewulf.diversity.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(SweetBerryBushBlock.class)
public class sneakingThroughSweetBerry {

    @Inject(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;makeStuckInBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/Vec3;)V", shift = At.Shift.AFTER), cancellable = true)
    private void onEntityCollisionMixin(BlockState state, Level world, BlockPos pos, Entity entity, CallbackInfo ci) {

        if (!ModConfigs.softer_sweetBery)
            return;

        if (!world.isClientSide && entity instanceof ServerPlayer serverPlayerEntity) {
            if (serverPlayerEntity.isShiftKeyDown()) {
                ci.cancel();
                return;
            }

            //if have pants
            if (serverPlayerEntity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem) {
                ci.cancel();
                return;
            }
        }
    }
}
