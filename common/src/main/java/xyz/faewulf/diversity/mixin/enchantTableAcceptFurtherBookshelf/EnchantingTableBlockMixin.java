package xyz.faewulf.diversity.mixin.enchantTableAcceptFurtherBookshelf;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(EnchantmentTableBlock.class)
public class EnchantingTableBlockMixin {

    @Shadow
    public static @Final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-3, -1, -3, 3, 1, 3)
            .filter(pos -> Math.abs(pos.getX()) >= 2 || Math.abs(pos.getZ()) >= 2)
            .map(BlockPos::immutable)
            .toList();
}
