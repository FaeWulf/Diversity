package xyz.faewulf.diversity.mixin.item.spyglassWhatIsThat;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityMixin {

    @Accessor
    Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> getRecipesUsed();
}
