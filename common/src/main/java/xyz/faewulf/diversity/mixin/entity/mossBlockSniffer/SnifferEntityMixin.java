package xyz.faewulf.diversity.mixin.entity.mossBlockSniffer;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.faewulf.diversity.util.CustomLootTables;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Sniffer.class)
public abstract class SnifferEntityMixin extends Animal {

    protected SnifferEntityMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract BlockPos getHeadBlock();

    @ModifyArg(method = "dropSeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/sniffer/Sniffer;dropFromGiftLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Ljava/util/function/BiConsumer;)Z"), index = 1)
    private ResourceKey<LootTable> modifyLootTable(ResourceKey<LootTable> originalLootTable) {

        if (!ModConfigs.sniffer_get_spore)
            return originalLootTable;

        if (this.level() instanceof ServerLevel serverLevel) {
            BlockPos target = this.getHeadBlock().below();
            if (serverLevel.getBlockState(target).getBlock() == Blocks.MOSS_BLOCK) {
                return CustomLootTables.SNIFFER_MOSS_BLOCK;
            }
        }

        return originalLootTable;
    }
}
