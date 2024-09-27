package xyz.faewulf.diversity.mixin.core.PseudoBlockEntity;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.feature.entity.pseudoBlockEntity.PseudoBlockEntity;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

    @Shadow
    @Final
    private Entity entity;

    //prevent pseudoBlockEntity send to client
    @Inject(method = "addPairing", at = @At("HEAD"), cancellable = true)
    private void addPairingInject(ServerPlayer pPlayer, CallbackInfo ci) {
        if (this.entity instanceof PseudoBlockEntity pseudoBlockEntity) {
            if (pseudoBlockEntity.diversity_Multiloader$getEntityType() != null)
                ci.cancel();
        }
    }
}
