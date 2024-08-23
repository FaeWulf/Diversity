package xyz.faewulf.diversity.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(ServerPlayer.class)
public abstract class PreventSetSpawnPoint {

    @Shadow
    public abstract void sendSystemMessage(Component pComponent);

    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void setSpawnPointMixin(ResourceKey<Level> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {

        if (!ModConfigs.prevent_setSpawn_onSleep)
            return;

        if (((Player) (Object) this).isShiftKeyDown()) {
            this.sendSystemMessage(Component.nullToEmpty("Respawn point set skipped. Release sneaking will disable this function."));
            ci.cancel();
        }
    }
}