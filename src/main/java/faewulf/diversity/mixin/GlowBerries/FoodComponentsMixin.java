package faewulf.diversity.mixin.GlowBerries;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodComponents.class)
public class FoodComponentsMixin {
    @Shadow
    public static final FoodComponent GLOW_BERRIES = ModConfigs.glow_berry_glowing ?
            new FoodComponent.Builder()
                    .nutrition(2)
                    .saturationModifier(0.1F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0), 0.6F)
                    .build()
            : new FoodComponent.Builder().nutrition(2).saturationModifier(0.1F).build();
}
