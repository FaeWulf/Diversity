package faewulf.diversity.mixin.spyglassWhatIsThat;

import net.minecraft.block.spawner.TrialSpawnerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrialSpawnerData.class)
public interface TrialSpawnerDataMixin {
    @Accessor
    long getCooldownEnd();
}
