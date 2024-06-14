package faewulf.diversity.mixin;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PreventSetSpawnPoint {

    @Shadow
    public abstract void sendMessage(Text message);

    @Inject(method = "setSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void setSpawnPointMixin(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {

        if (!ModConfigs.prevent_setSpawn_onSleep)
            return;

        if (((PlayerEntity) (Object) this).isSneaking()) {
            this.sendMessage(Text.of("Respawn point set skipped. Release sneaking will disable this function."));
            ci.cancel();
        }
    }
}