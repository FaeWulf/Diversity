package faewulf.diversity.mixin;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SweetBerryBushBlock.class)
public class sneakingThroughSweetBerry {

    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;slowMovement(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)V", shift = At.Shift.AFTER), cancellable = true)
    private void onEntityCollisionMixin(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {

        if (!ModConfigs.softer_sweetBery)
            return;

        if (!world.isClient && entity instanceof ServerPlayerEntity serverPlayerEntity) {
            if (serverPlayerEntity.isSneaking()) {
                ci.cancel();
                return;
            }

            //if have pants
            if (serverPlayerEntity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof ArmorItem) {
                ci.cancel();
                return;
            }
        }
    }
}
