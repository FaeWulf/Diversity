package xyz.faewulf.diversity.mixin.WanderingTraderAnnouncer;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderManagerMixin implements CustomSpawner {
    @Inject(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/WanderingTrader;setDespawnDelay(I)V"))
    private void trySpawnInject(ServerLevel world, CallbackInfoReturnable<Boolean> cir, @Local Player player) {

        if (!ModConfigs.wandering_trader_announcer)
            return;

        if (world.isClientSide)
            return;

        player.sendSystemMessage(Component.literal("A wandering trader has arrived!").withStyle(ChatFormatting.BLUE));
    }
}
