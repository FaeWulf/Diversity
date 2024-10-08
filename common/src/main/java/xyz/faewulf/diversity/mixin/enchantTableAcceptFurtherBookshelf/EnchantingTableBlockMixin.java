package xyz.faewulf.diversity.mixin.enchantTableAcceptFurtherBookshelf;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.mixinPlugin.ConditionalMixin;

import java.util.List;

@ConditionalMixin(configClass = ModConfigs.class, fieldName = "bigger_radius_bookshelf_for_enchantingTable")
@Mixin(EnchantmentTableBlock.class)
public class EnchantingTableBlockMixin {

    @Shadow
    public static final @Final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-3, -1, -3, 3, 1, 3)
            .filter(pos -> Math.abs(pos.getX()) >= 2 || Math.abs(pos.getZ()) >= 2)
            .map(BlockPos::immutable)
            .toList();
}
