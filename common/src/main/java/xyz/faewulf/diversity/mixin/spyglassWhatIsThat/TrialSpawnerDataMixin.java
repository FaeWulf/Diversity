package xyz.faewulf.diversity.mixin.spyglassWhatIsThat;

import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrialSpawnerData.class)
public interface TrialSpawnerDataMixin {
    @Accessor
    long getCooldownEndsAt();
}
