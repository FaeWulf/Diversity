package faewulf.diversity.mixin.dayCounter;

import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements StructureWorldAccess, AttachmentTarget {
    @Shadow
    public abstract List<ServerPlayerEntity> getPlayers();

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V"))
    private void tickTimeInject(CallbackInfo ci) {

        if (!ModConfigs.day_counter)
            return;

        if (this.getDimension().hasSkyLight() && this.getTimeOfDay() % 24000L == 0) {
            for (ServerPlayerEntity player : this.getPlayers()) {
                player.sendMessage(Text.literal("Day #" + (this.getTimeOfDay() / 24000L + 1L) + " has come!").formatted(Formatting.GOLD), true);
            }
        }
    }
}
