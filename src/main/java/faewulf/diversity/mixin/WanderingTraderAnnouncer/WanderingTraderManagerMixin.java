package faewulf.diversity.mixin.WanderingTraderAnnouncer;

import com.llamalad7.mixinextras.sugar.Local;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderManager.class)
public class WanderingTraderManagerMixin {
    @Inject(method = "trySpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/WanderingTraderEntity;setDespawnDelay(I)V"))
    private void trySpawnInject(ServerWorld world, CallbackInfoReturnable<Boolean> cir, @Local PlayerEntity player) {

        if (!ModConfigs.wandering_trader_announcer)
            return;

        if (world.isClient)
            return;

        player.sendMessage(Text.literal("A wandering trader has arrived!").formatted(Formatting.BLUE));
    }
}
